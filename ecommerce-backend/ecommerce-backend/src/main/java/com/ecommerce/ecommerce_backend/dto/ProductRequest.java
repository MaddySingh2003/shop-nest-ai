package com.ecommerce.ecommerce_backend.dto;

import lombok.Data;

@Data
public class ProductRequest {
     private String name;
    private String description;
    private String brand;
    private double price;
    private String imageUrl;
 private String category;
}
