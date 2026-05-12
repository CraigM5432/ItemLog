package com.itemlog.itemlogapi.repository;

import com.itemlog.itemlogapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository for the User entity.
// Spring Data JPA automatically creates the implementation at runtime.
public interface UserRepository extends JpaRepository<User, Integer> {

    // Checks whether a username already exists.
    // Used during registration to prevent duplicates.
    boolean existsByUsername(String username);

    // Checks whether an email already exists.
    // Used during registration to prevent duplicates.
    boolean existsByEmail(String email);

    // Finds a user by username.
    // Used during login to retrieve the user's password hash.
    Optional<User> findByUsername(String username);
}