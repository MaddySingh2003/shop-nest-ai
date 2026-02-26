package com.ecommerce.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private double discountPercent;
    private double maxDiscount;

    private LocalDateTime expiryDate;

    private Boolean active; // ✅ IMPORTANT FIX

    @Builder.Default
    private Integer usageLimit = 10;

    @Builder.Default
    private Integer usedCount = 0;

    @ManyToOne
    private Product product;
}