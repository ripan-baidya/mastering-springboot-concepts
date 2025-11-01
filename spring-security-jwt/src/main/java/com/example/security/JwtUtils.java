package com.example.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility class containing methods for working with JSON Web Tokens (JWT).
 * Provides functionality for generating, validating, and extracting information from JWTs.
 */
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String secret; // Jwt secret key

    @Value("${app.jwt.expiration}")
    private Long expiration; // Jwt expiration in milliseconds

    private SecretKey key;

    // Initializes the key after the class initiated and jwt secret is loaded
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JSON Web Token (JWT) based on the provided username.
     * The username can be any unique identifier depending on the application's requirements.
     * The generated JWT token contains the username as the subject and is signed with the secret key.
     * The token is valid for the specified expiration time.
     *
     * @param username the username to use as the subject of the JWT token
     * @return the generated JWT token
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        return
                Jwts.builder()
                        // The "sub" (subject) claim identifies the principal that is the subject of the JWT.
                        .subject(username)
                        // The “iss” claim is like a tag that identifies the person or company that created the JWT.
                        // How you handle this claim depends on your app.
                        .issuer("api")
                        // The "iat" (issued at) claim identifies the time at which the JWT was issued.
                        .issuedAt(now)
                        // The "exp" (expiration time) claim identifies the expiration time on or after which the JWT
                        // must not be accepted.
                        .expiration(expiry)
                        // The "signWith" method is used to sign the JWT with a secret key.
                        .signWith(key)
                        // The "compact" method is used to generate the JWT token.
                        .compact();
    }

    /**
     * Extracts the username from the provided JSON Web Token (JWT).
     * The username is the subject of the JWT token and is used as a unique identifier for the user.
     * The method verifies the JWT token with the secret key and then extracts the username.
     *
     * @param token the JWT token to extract the username from
     * @return the extracted username or null if the token is invalid or expired
     */
    public String getUsernameFromToken(String token) {
        return
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject(); // username

    }

    /**
     * Validates the provided JSON Web Token (JWT) and returns a boolean indicating whether the token is valid.
     * The method checks if the token is expired and if the username extracted from the token matches the provided username.
     *
     * @param token the JWT token to validate
     * @param username the username to validate against
     * @return true if the token is valid, false otherwise
     */
    /*
    public boolean validateToken(String token, String username) {
        String extractedUsername = getUsernameFromToken(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid token!" + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if the provided JSON Web Token (JWT) is expired.
     * The method parses the JWT token and extracts the expiration date from the token.
     * If the expiration date is before the current date, the token is considered expired.
     *
     * @param token the JWT token to check for expiration
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }
}
