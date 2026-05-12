package com.itemlog.itemlogapi.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

// DTO used when the frontend sends a request to update an existing item.
// Fields are optional because the user may update only one or some item details.
public class UpdateItemRequest {

    // Optional item name.
    // If provided, it must not be longer than 100 characters.
    @Size(max = 100, message = "name must be at most 100 characters.")
    private String name;

    // Optional item price.
    // BigDecimal is used for accurate currency values.
    @DecimalMin(value = "0.00", inclusive = true, message = "price must be 0.00 or greater.")
    @Digits(integer = 8, fraction = 2, message = "price must have up to 8 digits and 2 decimals.")
    private BigDecimal price;

    // Optional size field.
    @Size(max = 20, message = "size must be at most 20 characters.")
    private String size;

    // Optional quantity field.
    // It cannot be negative, if provided.
    @Min(value = 0, message = "quantity must be 0 or greater.")
    private Integer quantity;

    @Size(max = 255, message = "imagePath must be at most 255 characters.")
    private String imagePath;

    // Optional longer description for the item.
    private String description;

    public UpdateItemRequest() {}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public String getSize() { return size; }

    public void setSize(String size) { this.size = size; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}

