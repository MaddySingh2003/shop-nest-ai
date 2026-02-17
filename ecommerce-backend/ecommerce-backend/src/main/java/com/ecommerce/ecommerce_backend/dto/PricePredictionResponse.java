package com.ecommerce.ecommerce_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PricePredictionResponse {
    @JsonProperty("predicted_price")
    private double predictedPrice;
}
