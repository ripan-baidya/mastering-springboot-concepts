package com.example.config;

import com.example.security.JwtAuthenticationEntryPoint;
import com.example.security.JwtAuthenticationFilter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // disable csrf
                .cors(cors -> cors.disable())
                /**
                 * In order to access the h2 db console, we need to add the below code. The reason is that by default, Spring Security sets
                 * the X-Frame-Options response header to "DENY" in order to prevent clickjacking attacks. This means that the browser will
                 * not allow the page to be rendered in a frame. In order to access the h2 db console, we need to allow the page to be
                 * rendered in a frame. So we need to add the below code to allow the page to be rendered in a frame.
                 */
                // .headers((headers) -> headers.frameOptions().sameOrigin())
                .sessionManagement(sessions ->
                        sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // mentioning how to handle different types of requests
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/h2-console/**", "/test", "/auth").permitAll()
                        .requestMatchers(
                                "/resources/**", // "/api/resources can be anything"
                                 "/products/**"
                        ).authenticated()
                        .anyRequest().permitAll()
                )
                // adding jwtAuthenticationFilter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // enabling the default http basic authentication
                // .httpBasic(Customizer.withDefaults())

                // enabling the default form login
                // .formLogin(Customizer.withDefaults())

                // logout
                // .logout().deleteCookies("remove").invalidateHttpSession(false)
                // .logoutUrl("/logout")
                // .logoutSuccessUrl("/success")
        ;
        return http.build();
    }

    /**
     * Returns an implementation of UserDetailsService that stores user details in memory.
     * This implementation is used for testing purposes only and should not be used in a production environment.
     */
    // When we implement CustomeUserDetailsService at that moment we don not need to use this @Bean
    // @Bean
    public UserDetailsService userDetailsService() {
        /**
         * While storing the password we should use PassWordEncoder to encode the password otherwise we wont able to
         * access the api or might get exception. in production, we must use passwordEncoder or should not leave the
         * password in its original format.
         * Note: NoOpPasswordEncoder is not recommended for production use, but it can be used for testing purposes.
         * With NoOpPasswordEncoder, the password is not encoded and can be used as it is.
         */
        UserDetails user = User.withUsername("user")
                /**
                 * If we use prefix {noop} then it will use NoOpPasswordEncoder, but this will not work, you will get
                 * something like "Encoded password does not look like BCrypt" in your console log.
                 * Spring Security 5+ defaults to BCryptPasswordEncoder for password matching unless otherwise configured.
                 */
                .password("{noop}pass")
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}pass")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Since we have our own custom user details service, we need to provide the authentication provider.
     * we have set the userDetailsService and passwordEncoder in the authenticationProvider. and return it.
     */
    // @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }

    /** for encoding password */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(14);
    }
}
