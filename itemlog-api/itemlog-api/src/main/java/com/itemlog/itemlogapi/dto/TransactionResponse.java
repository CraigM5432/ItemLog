package com.itemlog.itemlogapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Integer transactionId,
        Integer eventId,
        Integer itemId,
        String itemName,
        Integer quantitySold,
        BigDecimal salePrice,
        BigDecimal totalAmount,
        LocalDateTime saleTime
) {}
