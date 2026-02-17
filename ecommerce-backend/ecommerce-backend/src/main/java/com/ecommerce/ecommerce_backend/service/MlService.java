package com.ecommerce.ecommerce_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.ecommerce_backend.dto.PricePredictionRequest;
import com.ecommerce.ecommerce_backend.dto.PricePredictionResponse;

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
}
