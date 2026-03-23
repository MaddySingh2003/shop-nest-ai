package com.ecommerce.ecommerce_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_backend.model.UserCouponAttempt;

public interface UserCouponAttemptRepository 
        extends JpaRepository<UserCouponAttempt, Long> {

    Optional<UserCouponAttempt> findByEmail(String email);
}