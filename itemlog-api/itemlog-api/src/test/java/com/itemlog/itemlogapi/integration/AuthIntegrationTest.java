package com.itemlog.itemlogapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itemlog.itemlogapi.dto.LoginRequest;
import com.itemlog.itemlogapi.dto.RegisterRequest;
import com.itemlog.itemlogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Integration tests for the authentication API.
// These tests start the Spring Boot application and test the real HTTP endpoints.
@SpringBootTest

// Automatically configures MockMvc for performing HTTP requests.
@AutoConfigureMockMvc

// Uses the "test" Spring profile.
@ActiveProfiles("test")
class AuthIntegrationTest {

    // MockMvc simulates real HTTP requests without starting a real server.
    @Autowired
    private MockMvc mockMvc;

    // Converts Java objects into JSON request bodies.
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Real repository used during integration tests.
    @Autowired
    private UserRepository userRepository;

    // Cleans up test users before each test runs.
    @BeforeEach
    void cleanUp() {

        userRepository.findByUsername("integrationuser")
                .ifPresent(userRepository::delete);

        userRepository.findByUsername("duplicateuser")
                .ifPresent(userRepository::delete);
    }

    // Tests successful registration through the real API endpoint.
    @Test
    void register_WithValidDetails_ReturnsSuccessResponse() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("integrationuser");
        request.setEmail("integrationuser@gmail.com");
        request.setPassword("Password123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)

                        // Converts the DTO into JSON.
                        .content(objectMapper.writeValueAsString(request)))

                // Confirms HTTP 200 OK.
                .andExpect(status().isOk())

                // Confirms the response JSON contains the expected message.
                .andExpect(jsonPath("$.message").value("User registered successfully."));
    }

    // Tests registration failure when a username already exists.
    @Test
    void register_WithDuplicateUsername_ReturnsBadRequest() throws Exception {

        // Creates the first user.
        RegisterRequest firstRequest = new RegisterRequest();
        firstRequest.setUsername("duplicateuser");
        firstRequest.setEmail("duplicate1@gmail.com");
        firstRequest.setPassword("Password123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        // Attempts to register another user with the same username.
        RegisterRequest duplicateRequest = new RegisterRequest();
        duplicateRequest.setUsername("duplicateuser");
        duplicateRequest.setEmail("duplicate2@gmail.com");
        duplicateRequest.setPassword("Password123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))

                // Confirms HTTP 400 Bad Request.
                .andExpect(status().isBadRequest())

                // Confirms the correct error message is returned.
                .andExpect(jsonPath("$.message").value("Username already exists."));
    }

    // Tests successful login through the real API endpoint.
    @Test
    void login_WithValidCredentials_ReturnsToken() throws Exception {

        // Registers a user first.
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("integrationuser");
        registerRequest.setEmail("integrationuser@gmail.com");
        registerRequest.setPassword("Password123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Creates a login request.
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("integrationuser");
        loginRequest.setPassword("Password123!");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))

                // Confirms successful login.
                .andExpect(status().isOk())

                // Confirms the username is returned.
                .andExpect(jsonPath("$.username").value("integrationuser"))

                // Confirms a JWT token exists in the response.
                .andExpect(jsonPath("$.token").exists());
    }

    // Tests login failure when the password is incorrect.
    @Test
    void login_WithInvalidPassword_ReturnsUnauthorized() throws Exception {

        // Registers a user first.
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("integrationuser");
        registerRequest.setEmail("integrationuser@gmail.com");
        registerRequest.setPassword("Password123!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Attempts login using the wrong password.
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("integrationuser");
        loginRequest.setPassword("WrongPassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))

                // Confirms HTTP 401 Unauthorized.
                .andExpect(status().isUnauthorized())

                // Confirms the expected error message.
                .andExpect(jsonPath("$.message").value("Invalid credentials."));
    }
}