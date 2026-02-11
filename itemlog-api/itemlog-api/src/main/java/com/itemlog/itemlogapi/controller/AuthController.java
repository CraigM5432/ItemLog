package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.*;
import com.itemlog.itemlogapi.entity.User;
import com.itemlog.itemlogapi.repository.UserRepository;
import com.itemlog.itemlogapi.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = userRepo.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials."));
        }

        boolean ok = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!ok) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials."));
        }

        String token = jwtService.generateToken(user.getUserId(), user.getUsername());

        return ResponseEntity.ok(new LoginResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                token
        ));
    }

    // Keep your /register endpoint as-is (no change needed)
}


