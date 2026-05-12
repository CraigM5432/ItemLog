package com.itemlog.itemlogapi.dto;

// Generic DTO used when the API only needs to return a simple message.
public class MessageResponse {

    // Human-readable response message.
    private String message;

    // Empty constructor required for JSON serialization/deserialization.
    public MessageResponse() {}

    // Constructor used to quickly create a message response.
    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter for message.
    public String getMessage() {
        return message;
    }

    // Setter for message.
    public void setMessage(String message) {
        this.message = message;
    }
}

