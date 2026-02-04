package com.itemlog.itemlogapi.dto;

public class RegisterResponse {
    private String message;
    private Integer userId;
    private String username;
    private String email;

    public RegisterResponse() {}

    public RegisterResponse(String message, Integer userId, String username, String email) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
