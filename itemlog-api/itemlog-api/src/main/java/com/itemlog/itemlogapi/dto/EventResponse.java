package com.itemlog.itemlogapi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EventResponse(
        Integer eventId,
        String eventName,
        LocalDate eventDate,
        LocalDateTime createdAt
) {}


