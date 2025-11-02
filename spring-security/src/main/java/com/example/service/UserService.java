package com.example.service;

import com.example.dto.LoginRequest;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    /**
     * <p>Register:</p>
     * register a new user, make sure while registering a new user his password must be encrypted before saving into db.
     */
    public String register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "User already exist!";
        }
        // creating a new user
        User newUser = User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        userRepository.save(newUser);
        return "User registered successfully";
    }

    /**
     * <p>Login:</p>
     * @return  jwt token
     */
    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }
}
