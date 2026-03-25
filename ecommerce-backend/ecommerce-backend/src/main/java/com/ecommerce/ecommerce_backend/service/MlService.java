package com.ecommerce.ecommerce_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.ecommerce.ecommerce_backend.dto.PricePredictionRequest;
import com.ecommerce.ecommerce_backend.dto.PricePredictionResponse;
import com.ecommerce.ecommerce_backend.dto.RecommendationResponse;

import java.util.*;

@Service
public class MlService {

    private final String BASE_URL =
            System.getenv().getOrDefault(
                    "ML_SERVICE_URL",
                    "https://shop-nest-ai.onrender.com"
            );

    private final String PRICE_URL = BASE_URL + "/predict-price";

    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(60000);
        return new RestTemplate(factory);
    }

    private final RestTemplate restTemplate = createRestTemplate();

    // ================= PRICE =================
    public Double getPredictedPrice(PricePredictionRequest request){

        try {
            ResponseEntity<PricePredictionResponse> response =
                    restTemplate.postForEntity(
                            PRICE_URL,
                            request,
                            PricePredictionResponse.class
                    );

            if(response == null || response.getBody() == null){
                return request.getBasePrice();
            }

            Double predicted = response.getBody().getPredictedPrice();

            if(predicted == null || predicted <= 0 || predicted.isNaN()){
                return request.getBasePrice();
            }

            return predicted;

        } catch(Exception e){
            System.out.println("ML PRICE ERROR: " + e.getMessage());
            return request.getBasePrice();
        }
    }

    // ================= RECOMMEND BY ID =================
    public List<Map<String,Object>> getRecommendationsById(Long productId){

        try {
            String url = BASE_URL + "/recommend/" + productId;

            ResponseEntity<RecommendationResponse> response =
                    restTemplate.getForEntity(
                            url,
                            RecommendationResponse.class
                    );

            if(response == null || response.getBody() == null){
                return List.of();
            }

            return response.getBody().getRecommendations();

        } catch(Exception e){
            System.out.println("ML RECOMMEND ERROR: " + e.getMessage());
            return List.of();
        }
    }
}