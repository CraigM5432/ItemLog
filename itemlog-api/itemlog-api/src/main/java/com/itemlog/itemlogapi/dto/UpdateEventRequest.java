package com.itemlog.itemlogapi.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// DTO used when the frontend sends a request to update an existing event.
// Fields are optional because the user may update only the name or only the date.
public class UpdateEventRequest {

    // Optional event name.
    // If provided, it must not be longer than 100 characters.
    @Size(max = 100, message = "eventName must be at most 100 characters.")
    private String eventName;

    // Optional event date.
    // If provided, it must match the YYYY-MM-DD format.
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "eventDate must be in YYYY-MM-DD format."
    )
    private String eventDate;

    public UpdateEventRequest() {}

    // Constructor useful for testing or manually creating this DTO.
    public UpdateEventRequest(String eventName, String eventDate) {
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


