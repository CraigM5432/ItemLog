package com.itemlog.itemlogapi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Response DTO returned when the API sends event data to the frontend.
// A Java record is used because this is a simple immutable data carrier.
public record EventResponse(

        // Database ID of the event.
        Integer eventId,

        // Name of the event.
        String eventName,

        // Date of the event.
        LocalDate eventDate,

        // Timestamp showing when the event was created.
        LocalDateTime createdAt
) {}


