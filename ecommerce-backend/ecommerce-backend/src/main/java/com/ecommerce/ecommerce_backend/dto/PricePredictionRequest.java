package com.ecommerce.ecommerce_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PricePredictionRequest {

    private String category;

    private String brand;

    @JsonProperty("base_price")
    private double basePrice;

    @JsonProperty("demand_score")
    private double demandScore;

    private double rating;
}