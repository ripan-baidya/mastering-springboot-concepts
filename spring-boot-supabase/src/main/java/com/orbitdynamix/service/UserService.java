package com.orbitdynamix.service;

import com.orbitdynamix.model.User;
import com.orbitdynamix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    // create a new user
    public User createUser(User user) {
        var savedUser = User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        savedUser = userRepository.save(savedUser); // persist data into db
        log.info("User saved successfully with id {}", savedUser.getId());
        return savedUser;
    }

    // get user by id
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found with id {}", id);
            return new RuntimeException("User not found with id " + id);
        });
    }

    // get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User not found with email {} ", email);
            return new RuntimeException("User not found with id " + email);
        });
    }

    // delete user by id
    public void deleteUser(UUID id) {
        User user = getUserById(id);

        log.info("Delete user with id {}", id);
        userRepository.delete(user);
    }

    // update user by id
    public User updateUser(UUID id, User user) {
        User existingUser = getUserById(id);
        if (user.getFullName() != null || user.getFullName().isEmpty())
            existingUser.setFullName(user.getFullName());
        if (user.getEmail() != null || user.getEmail().isEmpty())
            existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null || user.getPassword().isEmpty())
            existingUser.setPassword(user.getPassword());

        log.info("User updated successfully.");
        return existingUser;
    }

    // get all users
    public List<User> getAllUsers() {
        log.info("All users found.");
        return userRepository.findAll();
    }
}
