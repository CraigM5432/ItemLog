package com.itemlog.itemlogapi.dto;

import java.util.Map;

// DTO used when request validation fails.
// It returns a general message plus field-specific validation errors.
public class ValidationErrorResponse {

    // General validation failure message.
    private String message;

    // Map of field names to their validation error messages.
    // Example: "email" -> "Email must be valid"
    private Map<String, String> fieldErrors;

    // Empty constructor required for JSON serialization/deserialization.
    public ValidationErrorResponse() {
    }

    // Constructor used to quickly create a validation error response.
    public ValidationErrorResponse(String message, Map<String, String> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}