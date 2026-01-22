package com.ecommerce.ecommerce_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order reference
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private Order order;

    // Product Snapshot
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
}
