package com.ecommerce.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
@JsonProperty("default")
private boolean isDefault;

    @ManyToOne
    private User user;
}
