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

    @Column(unique=true)
    private String code;

    private double discountPercent;   // Example: 10 = 10%
    private double maxDiscount;       // Optional safety cap

    private LocalDateTime expiryDate;
    private boolean active;

   @Builder.Default
private int usageLimit = 10;

@Builder.Default
private int usedCount = 0;
}
