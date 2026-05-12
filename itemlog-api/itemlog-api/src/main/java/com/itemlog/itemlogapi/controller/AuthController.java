package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.*;
import com.itemlog.itemlogapi.entity.User;
import com.itemlog.itemlogapi.repository.UserRepository;
import com.itemlog.itemlogapi.security.JwtService;
import com.itemlog.itemlogapi.security.ratelimit.LoginRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// Handles public authentication routes: registration and login.
// Responsible for password hashing, credential checking, JWT generation and login rate limiting.
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginRateLimiter rateLimiter;

    public AuthController(UserRepository userRepo,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          LoginRateLimiter rateLimiter) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username already exists."));
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email already exists."));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // Passwords are stored as BCrypt hashes, never as plain text.
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
                                   HttpServletRequest httpRequest) {

        String clientIp = httpRequest.getRemoteAddr();

        // Limits repeated login attempts from the same IP address.
        if (!rateLimiter.isAllowed(clientIp)) {
            return ResponseEntity.status(429)
                    .body(new ErrorResponse("Too many login attempts. Please try again later."));
        }

        User user = userRepo.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials."));
        }

        boolean ok = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!ok) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials."));
        }

        // JWT contains the authenticated user's username and userId for later ownership checks.
        String token = jwtService.generateToken(user.getUserId(), user.getUsername());

        return ResponseEntity.ok(new LoginResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                token
        ));
    }
}