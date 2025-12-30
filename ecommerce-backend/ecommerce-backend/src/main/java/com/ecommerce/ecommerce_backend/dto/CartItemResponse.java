package com.ecommerce.ecommerce_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
}
