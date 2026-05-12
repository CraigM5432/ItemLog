package com.itemlog.itemlogapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Response DTO returned when the API sends transaction data to the frontend.
// A Java record is used because this is a simple immutable data carrier.
public record TransactionResponse(

        // Database ID of the transaction.
        Integer transactionId,

        // ID of the event where the transaction happened.
        Integer eventId,

        // ID of the item that was sold.
        Integer itemId,

        // Name of the item that was sold.
        String itemName,

        // Number of units sold.
        Integer quantitySold,

        // Sale price used for this transaction.
        BigDecimal salePrice,

        // Total sale amount, calculated as quantitySold multiplied by salePrice.
        BigDecimal totalAmount,

        // Timestamp showing when the sale was recorded.
        LocalDateTime saleTime
) {}