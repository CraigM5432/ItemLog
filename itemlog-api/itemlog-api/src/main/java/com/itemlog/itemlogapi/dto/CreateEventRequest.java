package com.itemlog.itemlogapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Request DTO for creating a new event.
public class CreateEventRequest {

    // Event name is required and cannot be blank.
    // It is limited to 100 characters to match input limits.
    @NotBlank(message = "eventName is required.")
    @Size(max = 100, message = "eventName must be at most 100 characters.")
    private String eventName;

    // Event date is required.
    // The regex ensures the frontend sends the date as YYYY-MM-DD.
    @NotBlank(message = "eventDate is required.")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "eventDate must be in YYYY-MM-DD format."
    )
    private String eventDate;


    public CreateEventRequest() {}

    // Convenience constructor useful for testing or manually creating this DTO.
    public CreateEventRequest(String eventName, String eventDate) {
        this.eventName = eventName;
        this.eventDate = eventDate;
    }


    public String getEventName() {
        return eventName;
    }


    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public String getEventDate() {
        return eventDate;
    }


    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}