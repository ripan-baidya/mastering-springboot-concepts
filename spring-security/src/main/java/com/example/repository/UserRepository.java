package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by his email
    Optional<User> findByEmail(String email);

    // Checking whether a user with the given email exists
    boolean existsByEmail(String email);
}
