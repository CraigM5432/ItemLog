package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.ErrorResponse;
import com.itemlog.itemlogapi.dto.LoginRequest;
import com.itemlog.itemlogapi.dto.LoginResponse;
import com.itemlog.itemlogapi.dto.RegisterRequest;
import com.itemlog.itemlogapi.dto.RegisterResponse;
import com.itemlog.itemlogapi.entity.User;
import com.itemlog.itemlogapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("All fields are required."));
        }

        String username = request.getUsername().trim();
        String email = request.getEmail().trim();

        if (repo.existsByUsername(username)) {
            return ResponseEntity.status(409).body(new ErrorResponse("Username already exists."));
        }
        if (repo.existsByEmail(email)) {
            return ResponseEntity.status(409).body(new ErrorResponse("Email already exists."));
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User saved = repo.save(user);

        RegisterResponse response = new RegisterResponse(
                "User registered",
                saved.getUserId(),
                saved.getUsername(),
                saved.getEmail()
        );

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username and password are required."));
        }

        User user = repo.findByUsername(request.getUsername().trim()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid username or password."));
        }

        return ResponseEntity.ok(new LoginResponse(user.getUserId(), user.getUsername(), user.getEmail()));
    }
}

