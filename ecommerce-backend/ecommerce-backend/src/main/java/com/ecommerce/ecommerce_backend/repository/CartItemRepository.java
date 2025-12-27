package com.ecommerce.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_backend.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long>{
    
}
