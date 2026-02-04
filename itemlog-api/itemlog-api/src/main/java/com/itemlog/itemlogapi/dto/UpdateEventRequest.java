package com.itemlog.itemlogapi.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateEventRequest {

    @Size(max = 100, message = "eventName must be at most 100 characters.")
    private String eventName;

    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "eventDate must be in YYYY-MM-DD format."
    )
    private String eventDate;

    public UpdateEventRequest() {}

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


