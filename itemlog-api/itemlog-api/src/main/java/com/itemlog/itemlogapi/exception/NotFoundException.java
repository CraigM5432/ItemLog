package com.itemlog.itemlogapi.exception;

// Custom runtime exception used when a requested resource cannot be found.
// Ex. user not found, event not found, or item not found.
public class NotFoundException extends RuntimeException {

    // Passes the custom error message to the parent RuntimeException class.
    public NotFoundException(String message) {
        super(message);
    }
}