package com.ecommerce.ecommerce_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_backend.model.VerificationToken;

public interface VerificationTokenRepository 
             extends JpaRepository<VerificationToken,Long>{
    Optional<VerificationToken> findByToken(String token);
    
}
