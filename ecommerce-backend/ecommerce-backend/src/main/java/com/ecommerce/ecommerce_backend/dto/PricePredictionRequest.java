package com.ecommerce.ecommerce_backend.dto;

import lombok.Data;

@Data
public class PricePredictionRequest {
    private String category;
    private String brand;
    private double base_price;
    private double demand_score;
    private double rating;
}
