package com.ecommerce.ecommerce_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.ecommerce.ecommerce_backend.dto.PricePredictionRequest;
import com.ecommerce.ecommerce_backend.dto.PricePredictionResponse;
import com.ecommerce.ecommerce_backend.dto.RecommendationRequest;
import com.ecommerce.ecommerce_backend.dto.RecommendationResponse;

import java.util.*;

@Service
public class MlService {

    // ✅ USE ENV VARIABLE (PRODUCTION SAFE)
    private final String BASE_URL =
            System.getenv().getOrDefault(
                    "ML_SERVICE_URL",
                    "https://shop-nest-ai.onrender.com"
            );

    private final String PRICE_URL = BASE_URL + "/predict-price";
    private final String RECOMMEND_URL = BASE_URL + "/recommend";

    // ✅ TIMEOUT CONFIG (IMPORTANT FOR RENDER)
    private RestTemplate createRestTemplate() {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(10000);
        factory.setReadTimeout(60000);

        return new RestTemplate(factory);
    }

    private final RestTemplate restTemplate = createRestTemplate();

    // ===============================
    // 🔥 PRICE PREDICTION
    // ===============================
    public Double getPredictedPrice(PricePredictionRequest request){

        try {

            ResponseEntity<PricePredictionResponse> response =
                    restTemplate.postForEntity(
                            PRICE_URL,
                            request,
                            PricePredictionResponse.class
                    );
System.out.println("ML PRICE = " + 
    (response.getBody() != null ? response.getBody().getPredictedPrice() : "NULL"));

            if(response.getBody() == null ||response==null){
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

    // ===============================
    // 🔥 RECOMMENDATION
    // ===============================
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