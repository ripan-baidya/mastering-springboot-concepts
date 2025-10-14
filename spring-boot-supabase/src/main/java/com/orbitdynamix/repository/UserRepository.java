package com.orbitdynamix.repository;

import com.orbitdynamix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // get user by email
    Optional<User> findByEmail(String email);
}
