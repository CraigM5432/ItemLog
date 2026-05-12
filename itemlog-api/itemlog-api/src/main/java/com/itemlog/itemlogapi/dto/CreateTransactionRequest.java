package com.itemlog.itemlogapi.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

// DTO used when the frontend sends a request to record a sale transaction.
public class CreateTransactionRequest {

    // ID of the item being sold.
    // The backend uses this to find the correct item inside the selected event.
    @NotNull(message = "itemId is required.")
    private Integer itemId;

    // Number of units sold.
    // Must be at least 1 because a sale cannot have a zero or negative quantity.
    @NotNull(message = "quantitySold is required.")
    @Min(value = 1, message = "quantitySold must be at least 1.")
    private Integer quantitySold;

    // Optional sale price.
    // If this is null, the backend uses the item's current price.
    @DecimalMin(value = "0.00", inclusive = true, message = "salePrice must be 0.00 or greater.")
    @Digits(integer = 8, fraction = 2, message = "salePrice must have up to 8 digits and 2 decimals.")
    private BigDecimal salePrice;

    public CreateTransactionRequest() {}

    public Integer getItemId() { return itemId; }

    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public Integer getQuantitySold() { return quantitySold; }

    public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }

    public BigDecimal getSalePrice() { return salePrice; }

    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
}
