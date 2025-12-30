package com.ecommerce.ecommerce_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long id;
    private String status;
    private double totalAmount;
    private LocalDateTime orderDate;

    private String shippingName;
    private String shippingCity;
    private String shippingState;
    private String shippingCountry;

    private List<OrderItemResponse> items;
}
