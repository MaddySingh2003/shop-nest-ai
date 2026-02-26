package com.ecommerce.ecommerce_backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.model.UserCouponAttempt;
import com.ecommerce.ecommerce_backend.repository.CouponRepository;
import com.ecommerce.ecommerce_backend.repository.UserCouponAttemptRepository;

import lombok.*;
@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponAttemptRepository repo;
    private final CouponRepository couponRepository;
public Coupon tryLuck(String email){

    UserCouponAttempt attempt = repo.findByEmail(email)
        .orElseGet(() -> {
            UserCouponAttempt a = new UserCouponAttempt();
            a.setEmail(email);
            a.setAttempts(0);
            return a;
        });

    // RESET AFTER 5 DAYS
    if(attempt.getLastAttempt() != null &&
       attempt.getLastAttempt().plusDays(5).isBefore(LocalDateTime.now())){
        attempt.setAttempts(0);
    }

    if(attempt.getAttempts() >= 2){
        throw new RuntimeException("Try after 5 days");
    }

    attempt.setAttempts(attempt.getAttempts() + 1);
    attempt.setLastAttempt(LocalDateTime.now());

    repo.save(attempt);

    System.out.println("User: " + email + " Attempts: " + attempt.getAttempts());

    // RANDOM WIN
    boolean win = Math.random() < 0.5;

    if(!win){
        throw new RuntimeException("Better luck next time 😢");
    }

    Coupon coupon = couponRepository.findRandomActiveCoupon();

    if(coupon == null){
        throw new RuntimeException("No coupons available");
    }

    return coupon;
}}