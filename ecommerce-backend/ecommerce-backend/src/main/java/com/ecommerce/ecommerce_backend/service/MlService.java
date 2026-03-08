package com.ecommerce.ecommerce_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.ecommerce_backend.dto.PricePredictionRequest;
import com.ecommerce.ecommerce_backend.dto.PricePredictionResponse;
import com.ecommerce.ecommerce_backend.dto.RecommendationRequest;
import com.ecommerce.ecommerce_backend.dto.RecommendationResponse;

import java.util.*;

@Service
public class MlService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String PRICE_URL = "http://localhost:8000/predict-price";
    private final String RECOMMEND_URL = "http://localhost:8000/recommend";

   public Double getPredictedPrice(PricePredictionRequest request){

    try {

        ResponseEntity<PricePredictionResponse> response =
                restTemplate.postForEntity(
                        PRICE_URL,
                        request,
                        PricePredictionResponse.class
                );

        System.out.println("ML RESPONSE = " + response.getBody());

        if(response.getBody() == null){
            return request.getBasePrice();
        }

        Double predicted = response.getBody().getPredictedPrice();

        if(predicted == null || predicted <= 0 || predicted.isNaN()){
            return request.getBasePrice();
        }

        return predicted;

    } catch(Exception e){

        System.out.println("ML ERROR: " + e.getMessage());

        return request.getBasePrice();
    }
}
    public List<Map<String,String>> getRecommendations(String description){

        try{

            RecommendationRequest req = new RecommendationRequest();
            req.setDescription(description);

            ResponseEntity<RecommendationResponse> response =
                    restTemplate.postForEntity(
                            RECOMMEND_URL,
                            req,
                            RecommendationResponse.class
                    );

            if(response == null || response.getBody() == null){
                return List.of();
            }

            return response.getBody().getRecommendations();

        }
        catch(Exception e){

            System.out.println("Recommendation error: " + e.getMessage());

            return List.of();
        }
    }
}