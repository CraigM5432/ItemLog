package com.itemlog.itemlogapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Response DTO returned when the API sends item data to the frontend.
// A Java record is used because this is a simple immutable data carrier.
public record ItemResponse(

        // Database ID of the item.
        Integer itemId,

        // ID of the event that this item belongs to.
        Integer eventId,

        // Item name.
        String name,

        // Item price. BigDecimal is used for accurate currency values.
        BigDecimal price,

        // Optional item size.
        String size,

        // Current stock quantity.
        Integer quantity,

        String imagePath,

        // Optional item description.
        String description,

        // Timestamp showing when the item was created.
        LocalDateTime createdAt
) {}