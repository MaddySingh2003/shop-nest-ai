package com.ecommerce.ecommerce_backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean used;

    private LocalDateTime assignedAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Coupon coupon;
}