package com.itemlog.itemlogapi.dto;

// Generic DTO used when the API needs to return an error message.
public class ErrorResponse {

    // Human-readable error message.
    private String message;

    public ErrorResponse() {}

    // Constructor used to quickly create an error response.
    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}
