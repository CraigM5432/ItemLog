package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.LoginRequest;
import com.itemlog.itemlogapi.dto.RegisterRequest;
import com.itemlog.itemlogapi.entity.User;
import com.itemlog.itemlogapi.repository.UserRepository;
import com.itemlog.itemlogapi.security.JwtService;
import com.itemlog.itemlogapi.security.ratelimit.LoginRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private LoginRateLimiter rateLimiter;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_WithValidDetails_ReturnsOkStatus() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("testuser@gmail.com");
        request.setPassword("Password123!");

        when(userRepo.existsByUsername("testuser")).thenReturn(false);
        when(userRepo.existsByEmail("testuser@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("hashedPassword");

        ResponseEntity<?> response = authController.register(request);

        assertEquals(200, response.getStatusCode().value());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void register_WithExistingUsername_ReturnsBadRequestStatus() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setEmail("newemail@gmail.com");
        request.setPassword("Password123!");

        when(userRepo.existsByUsername("existinguser")).thenReturn(true);

        ResponseEntity<?> response = authController.register(request);

        assertEquals(400, response.getStatusCode().value());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void login_WithValidCredentials_ReturnsOkStatus() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("Password123!");

        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");
        user.setPasswordHash("bcryptHash");

        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(rateLimiter.isAllowed("127.0.0.1")).thenReturn(true);
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "bcryptHash")).thenReturn(true);
        when(jwtService.generateToken(1, "testuser")).thenReturn("fake-jwt-token");

        ResponseEntity<?> response = authController.login(request, httpRequest);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void login_WithInvalidPassword_ReturnsUnauthorizedStatus() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("WrongPassword");

        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");
        user.setPasswordHash("bcryptHash");

        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(rateLimiter.isAllowed("127.0.0.1")).thenReturn(true);
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPassword", "bcryptHash")).thenReturn(false);

        ResponseEntity<?> response = authController.login(request, httpRequest);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void login_WhenRateLimitExceeded_ReturnsTooManyRequestsStatus() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("Password123!");

        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(rateLimiter.isAllowed("127.0.0.1")).thenReturn(false);

        ResponseEntity<?> response = authController.login(request, httpRequest);

        assertEquals(429, response.getStatusCode().value());
        verify(userRepo, never()).findByUsername(anyString());
    }
}