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

// Unit test class for AuthController.
// Unit tests isolate the controller by mocking all dependencies.
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    // Mocked repository used instead of the real database.
    @Mock
    private UserRepository userRepo;

    // Mocked password encoder.
    @Mock
    private PasswordEncoder passwordEncoder;

    // Mocked JWT service.
    @Mock
    private JwtService jwtService;

    // Mocked rate limiter.
    @Mock
    private LoginRateLimiter rateLimiter;

    // Mocked HTTP request object.
    @Mock
    private HttpServletRequest httpRequest;

    // Creates the controller under test and injects the mocked dependencies above.
    @InjectMocks
    private AuthController authController;

    // Tests successful registration.
    @Test
    void register_WithValidDetails_ReturnsOkStatus() {

        // Creates a sample registration request.
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("testuser@gmail.com");
        request.setPassword("Password123!");

        // Mocks repository checks.
        when(userRepo.existsByUsername("testuser")).thenReturn(false);
        when(userRepo.existsByEmail("testuser@gmail.com")).thenReturn(false);

        // Mocks password hashing.
        when(passwordEncoder.encode("Password123!")).thenReturn("hashedPassword");

        // Calls the controller method.
        ResponseEntity<?> response = authController.register(request);

        // Confirms the HTTP status is 200 OK.
        assertEquals(200, response.getStatusCode().value());

        // Confirms the repository save method was called.
        verify(userRepo).save(any(User.class));
    }

    // Tests registration failure when the username already exists.
    @Test
    void register_WithExistingUsername_ReturnsBadRequestStatus() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setEmail("newemail@gmail.com");
        request.setPassword("Password123!");

        // Simulates an existing username.
        when(userRepo.existsByUsername("existinguser")).thenReturn(true);

        ResponseEntity<?> response = authController.register(request);

        // Confirms the HTTP status is 400 Bad Request.
        assertEquals(400, response.getStatusCode().value());

        // Confirms no user was saved.
        verify(userRepo, never()).save(any(User.class));
    }

    // Tests successful login.
    @Test
    void login_WithValidCredentials_ReturnsOkStatus() {

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("Password123!");

        // Creates a fake user object returned by the repository.
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");
        user.setPasswordHash("bcryptHash");

        // Mocks the client IP address.
        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        // Allows login attempt through the rate limiter.
        when(rateLimiter.isAllowed("127.0.0.1")).thenReturn(true);

        // Simulates finding the user in the database.
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Simulates password verification success.
        when(passwordEncoder.matches("Password123!", "bcryptHash")).thenReturn(true);

        // Simulates JWT generation.
        when(jwtService.generateToken(1, "testuser")).thenReturn("fake-jwt-token");

        ResponseEntity<?> response = authController.login(request, httpRequest);

        // Confirms successful login response.
        assertEquals(200, response.getStatusCode().value());
    }

    // Tests login failure when the password is incorrect.
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

        // Simulates password verification failure.
        when(passwordEncoder.matches("WrongPassword", "bcryptHash")).thenReturn(false);

        ResponseEntity<?> response = authController.login(request, httpRequest);

        // Confirms HTTP 401 Unauthorized.
        assertEquals(401, response.getStatusCode().value());
    }

    // Tests rate limiting protection.
    @Test
    void login_WhenRateLimitExceeded_ReturnsTooManyRequestsStatus() {

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("Password123!");

        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        // Simulates the rate limiter blocking the request.
        when(rateLimiter.isAllowed("127.0.0.1")).thenReturn(false);

        ResponseEntity<?> response = authController.login(request, httpRequest);

        // Confirms HTTP 429 Too Many Requests.
        assertEquals(429, response.getStatusCode().value());

        // Confirms the repository was never queried.
        verify(userRepo, never()).findByUsername(anyString());
    }
}