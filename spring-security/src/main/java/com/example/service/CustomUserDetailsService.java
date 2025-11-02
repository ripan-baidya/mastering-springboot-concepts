package com.example.service;

import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * Implementation class of {@code UserDetailsService}, it's responsible to load the username form the database.
 * In production environment we use our own customer user details service.
 */
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads user details by username.
     * <p>
     * The username can be an email, phone number, or any unique identifier
     * depending on the application's requirements.
     * </p>
     *
     * @param username the user's unique identifier (e.g., email or phone number)
     * @return a {@link org.springframework.security.core.userdetails.User} object
     *         or any custom implementation of {@link UserDetails}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.example.entity.User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("user name not found for email: %s", username));
        }
        return new User(
                user.get().getEmail(),
                user.get().getPassword(),
                Collections.emptyList()
        );
    }
}
