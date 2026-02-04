package com.itemlog.itemlogapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemResponse(
        Integer itemId,
        Integer eventId,
        String name,
        java.math.BigDecimal price,
        String size,
        Integer quantity,
        String imagePath,
        String description,
        java.time.LocalDateTime createdAt
) {}

