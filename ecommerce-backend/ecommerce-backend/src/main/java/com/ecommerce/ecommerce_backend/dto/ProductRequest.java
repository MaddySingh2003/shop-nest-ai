package com.ecommerce.ecommerce_backend.dto;

import lombok.Data;

@Data
public class ProductRequest {
      private String name;
    private String description;
    private String brand;
    private String category;
    private double price;
    private int stock;
    private String imageUrl;
}
