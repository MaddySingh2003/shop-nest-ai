package com.ecommerce.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who placed order
    @ManyToOne
    private User user;

    private double totalAmount;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        CONFIRMED,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
    
    @Column(nullable = false)
private String shippingName;

@Column(nullable = false)
private String shippingPhone;

@Column(nullable = false)
private String shippingStreet;

@Column(nullable = false)
private String shippingCity;

@Column(nullable = false)
private String shippingState;

@Column(nullable = false)
private String shippingZip;

@Column(nullable = false)
private String shippingCountry;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;
}
