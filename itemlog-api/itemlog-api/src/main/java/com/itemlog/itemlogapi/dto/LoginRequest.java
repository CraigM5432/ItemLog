package com.itemlog.itemlogapi.dto;

// DTO used when the frontend sends login details to /auth/login.
public class LoginRequest {

    // Username entered by the user on the login screen.
    private String username;

    // Password entered by the user on the login screen.
    private String password;

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
