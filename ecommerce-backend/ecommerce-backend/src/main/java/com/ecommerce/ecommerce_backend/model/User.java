package com.ecommerce.ecommerce_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@NotBlank
private String name;

@NotBlank
@Email
private String email;

@NotBlank
private String password;

@NotNull
@Enumerated(EnumType.STRING)
private Role role;
   
   public enum Role{
    USER,
    ADMIN
}
}
