package com.ecommerce.ecommerce_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

import com.ecommerce.ecommerce_backend.dto.PricePredictionRequest;
import com.ecommerce.ecommerce_backend.dto.PricePredictionResponse;
import com.ecommerce.ecommerce_backend.dto.RecommendationRequest;
import com.ecommerce.ecommerce_backend.dto.RecommendationResponse;

@Service
public class MlService {
    private final RestTemplate restTemplate=new RestTemplate();
    private final String ML_URL="http://localhost:8000/predict-price";


    public Double getPredictedPrice(PricePredictionRequest request){
        ResponseEntity<PricePredictionResponse>response=
        restTemplate.postForEntity(ML_URL, request, 
            PricePredictionResponse.class);
            return response.getBody().getPredictedPrice();
    }

public List<Map<String, String>> getRecommendations(String description) {

    String url = "http://localhost:8000/recommend";

    RecommendationRequest req = new RecommendationRequest();
    req.setDescription(description);

    ResponseEntity<RecommendationResponse> response =
            restTemplate.postForEntity(
                    url,
                    req,
                    RecommendationResponse.class
            );

    return response.getBody().getRecommendations();
}

}