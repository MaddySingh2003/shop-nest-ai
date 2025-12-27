package com.ecommerce.ecommerce_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.model.User;

public interface CartRepository extends JpaRepository<Cart,Long> {
    
   Optional<Cart>findByUser(User user);
}
