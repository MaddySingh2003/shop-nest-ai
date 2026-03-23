package com.ecommerce.ecommerce_backend.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class RecommendationResponse {
    private List <Map<String,String>>recommendations;
}
