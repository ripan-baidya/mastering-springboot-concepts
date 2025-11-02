# Implementing role-based JWT auth in Spring Boot 3.3.5 (Java 21+, JJWT 0.12.0+)

### What you’ll end up with

* A Spring Boot 3.3.5 app (Java 21+)
* Users with roles (`ROLE_USER`, `ROLE_ADMIN`) stored in DB
* Authentication endpoint that issues JWTs
* A `JwtUtils` using **jjwt 0.12.x** APIs
* A filter that validates tokens and populates `SecurityContext`
* Role-based protection of endpoints (e.g. `hasRole("ADMIN")`)

> Why JJWT 0.12.0+ matters: 0.12 introduced major new features (JWE/JWK support) and API changes/deprecations — so the examples below use the modern `Keys` + `Jwts.builder()/parserBuilder()` style. ([GitHub][2])

---

## 1) Create project & dependencies

Use Spring Initializr with:

* Spring Boot **3.3.5**
* Java **21**
* Dependencies: `spring-boot-starter-web`, `spring-boot-starter-security`, `spring-boot-starter-data-jpa`, `h2` (or your DB), `lombok` (optional), and jjwt libs.

Example `pom.xml` relevant parts:

```xml
<!-- parent and other stuff omitted -->
<properties>
  <java.version>21</java.version>
  <spring-boot.version>3.3.5</spring-boot.version>
  <jjwt.version>0.12.0</jjwt.version> <!-- use 0.12.x (or newer 0.12.*) -->
</properties>

<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>

  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
  </dependency>

  <!-- JJWT split artifacts (0.12.x style) -->
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>${jjwt.version}</version>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>${jjwt.version}</version>
    <scope>runtime</scope>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>${jjwt.version}</version>
    <scope>runtime</scope>
  </dependency>

  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
</dependencies>
```

(Using the split artifacts is the recommended pattern for 0.12.x+; see JJWT docs.) ([mvnrepository.com][3])

---

## 2) application.properties (simple example)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# JWT config
jwt.secret=change_this_to_a_very_long_random_secret_base64_or_hex
jwt.expiration-ms=3600000
```

**Important**: choose a secure, sufficiently large secret (for HS256 at least 256-bit key) or use an asymmetric key pair (recommended for production). JJWT provides helpers (`Keys`) to create correct SecretKey objects. ([javadoc.io][4])

---

## 3) Domain model (roles & users)

A minimal `Role` enum and `User` entity storing role(s):

```java
// Role.java
public enum Role {
  ROLE_USER,
  ROLE_ADMIN
}

// User.java (JPA)
@Entity
@Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
  @Id @GeneratedValue
  private Long id;

  @Column(unique=true, nullable=false)
  private String username;

  @Column(nullable=false)
  private String password; // stored as BCrypt

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name="user_roles", joinColumns=@JoinColumn(name="user_id"))
  @Enumerated(EnumType.STRING)
  private Set<Role> roles = new HashSet<>();
}
```

---

## 4) UserRepository + initial data

```java
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  boolean existsByUsername(String username);
}
```

Seed a couple of users at startup (passwords encoded with `BCryptPasswordEncoder`) so you can test `ROLE_USER` vs `ROLE_ADMIN`.

---

## 5) `UserDetailsService` — map roles -> authorities

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository repo) { this.userRepository = repo; }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .toList();

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        authorities
    );
  }
}
```

This ensures `userDetails.getAuthorities()` contains `ROLE_USER`, `ROLE_ADMIN` etc., and Spring's `hasRole("ADMIN")` works as expected.

---

## 6) JWT Utilities — **using JJWT 0.12.x** APIs

Important notes for 0.12.x:

* `SignatureAlgorithm` usage may be deprecated in favor of the newer API surface; using `Keys.hmacShaKeyFor(...)` and `Jwts.builder()` + `signWith(key)` is correct.
* Use `Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)` to parse/validate. ([javadoc.io][5])

Example `JwtUtils`:

```java
@Component
public class JwtUtils {
  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration-ms}")
  private long jwtExpirationMs;

  private SecretKey key;

  @PostConstruct
  public void init() {
    // You can store jwt.secret as base64 or plain string bytes.
    key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(Authentication authentication) {
    String username = authentication.getName();
    Date now = new Date();
    Date expiry = new Date(now.getTime() + jwtExpirationMs);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(now)
        .setExpiration(expiry)
        // If you want roles/cs in claims:
        .claim("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList())
        .signWith(key) // modern API
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
               .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      // handle log
      return false;
    }
  }
}
```

---

## 7) JWT authentication filter (extract token -> set SecurityContext)

```java
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
  private final JwtUtils jwtUtils;
  private final CustomUserDetailsService userDetailsService;

  public AuthTokenFilter(JwtUtils jwtUtils, CustomUserDetailsService uds) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = uds;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String header = req.getHeader("Authorization");
    String token = null;
    if (header != null && header.startsWith("Bearer ")) {
      token = header.substring(7);
    }
    if (token != null && jwtUtils.validateJwtToken(token)) {
      String username = jwtUtils.getUsernameFromToken(token);
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities()
      );
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    chain.doFilter(req, res);
  }
}
```

---

## 8) Security configuration (Spring Security 6 style)

```java
@Configuration
public class SecurityConfig {

  private final CustomUserDetailsService userDetailsService;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final AuthTokenFilter authTokenFilter;

  public SecurityConfig(CustomUserDetailsService uds, AuthEntryPointJwt entryPoint, AuthTokenFilter filter) {
    this.userDetailsService = uds;
    this.unauthorizedHandler = entryPoint;
    this.authTokenFilter = filter;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
          .requestMatchers("/api/auth/**").permitAll()
          .requestMatchers("/api/admin/**").hasRole("ADMIN")
          .requestMatchers("/api/user/**").hasAnyRole("USER","ADMIN")
          .anyRequest().authenticated()
      );

    http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
```

* Note `requestMatchers("/api/admin/**").hasRole("ADMIN")` uses role-based authorization.
* `.sessionCreationPolicy(SessionCreationPolicy.STATELESS)` — JWT is stateless.

---

## 9) Authentication controller (login -> JWT)

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest req) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
    );
    String token = jwtUtils.generateToken(auth);
    return ResponseEntity.ok(new AuthResponse(token));
  }
}
```

`AuthRequest` is a DTO with `username` and `password`. `AuthResponse` returns the token.

---

## 10) Protect endpoints by role

Example controller usage:

```java
@GetMapping("/admin/stats")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> adminOnly() { ... }

@GetMapping("/user/profile")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public ResponseEntity<?> userOrAdmin() { ... }
```

Or rely on the `SecurityFilterChain` `requestMatchers(...).hasRole(...)` rules defined earlier.

---

## 11) Testing

1. Start the app (H2 console available).
2. POST `/api/auth/login` with JSON `{ "username": "...", "password": "..." }`.
3. Receive JWT; include it in header `Authorization: Bearer <token>`.
4. Call `GET /api/admin/stats` and observe 403 unless the token owner has `ROLE_ADMIN`.

---

## 12) Production notes & recommendations

* **Use asymmetric keys (RSA/EC)** for production systems where services verify tokens (avoid a shared HMAC secret when possible).
* JJWT 0.12.x adds JWE/JWK features — useful if you need encryption or key rotation management via JWKs. If you adopt JWE/JWK, consult JJWT docs for correct setup. ([GitHub][2])
* Keep jjwt up-to-date within 0.12.x or later; watch breaking changes when upgrading (0.12 introduced some deprecated methods and stricter parsing). Several upgrade issues have been reported, so test thoroughly. ([GitHub][6])
* Prefer storing *only* a session identifier or minimal claims in JWTs; avoid storing sensitive user data in token claims.

---

## 13) Short FAQ (common gotchas)

**Q: Why split jjwt artifacts (api/impl/jackson)?**
A: From 0.11→0.12, JJWT split artifacts so you add `jjwt-api` at compile and `jjwt-impl` + `jjwt-jackson` at runtime. This is the recommended approach for 0.12.x+. ([mvnrepository.com][3])

**Q: My old `SignatureAlgorithm` usage is flagged as deprecated. What to use?**
A: Use `Keys` to create a `SecretKey` and `signWith(key)`; JJWT replaced some older API surfaces in 0.12. See JJWT Keys and Builder docs. ([javadoc.io][4])

**Q: What about Spring Boot 3.3.5 compatibility?**
A: Spring Boot 3.3.5 is stable and contains fixes and dependency upgrades — the Spring Security 6.x approach (SecurityFilterChain beans, `HttpSecurity` lambdas) used above is the correct style for Spring Boot 3.3.x. ([Home][7])

---

## Final code/package checklist

* `model` — `User`, `Role`
* `repository` — `UserRepository`
* `service` — `CustomUserDetailsService`
* `security` — `JwtUtils`, `AuthTokenFilter`, `AuthEntryPointJwt`, `SecurityConfig`
* `controller` — `AuthController`, plus protected controllers
* `dto` — `AuthRequest`, `AuthResponse`
* `application.properties` — secret, expiration, DB config
* `pom.xml` — Spring Boot 3.3.5, jjwt 0.12.x dependencies

-