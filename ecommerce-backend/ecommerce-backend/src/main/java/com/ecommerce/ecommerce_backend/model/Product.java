package com.ecommerce.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private double price;

    private String brand;

    private String imageUrl;

    private LocalDateTime createdAt;
    
@Column(nullable = false)
private int stock = 0;

@Column(name="predicted-price")
private Double predictedPrice;

private String category;
}
