package com.itemlog.itemlogapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO used when the frontend sends registration details to /auth/register.
public class RegisterRequest {

    // Username is required and cannot be blank.
    @NotBlank(message = "Username is required")
    private String username;

    // Email is required, cannot be blank, and must follow a valid email format.
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    // Password is required and must be at least 8 characters.
    // The controller hashes this password before saving it.
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}