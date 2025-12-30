package com.ecommerce.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Status {
        PENDING,
        SUCCESS,
        FAILED
    }

    @OneToOne
    @JsonIgnore
    private Order order;

    private double amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
}
