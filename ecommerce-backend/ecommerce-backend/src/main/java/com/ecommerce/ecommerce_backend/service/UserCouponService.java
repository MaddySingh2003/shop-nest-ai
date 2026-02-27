package com.ecommerce.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.model.UserCoupon;
import com.ecommerce.ecommerce_backend.model.UserCouponAttempt;
import com.ecommerce.ecommerce_backend.repository.CouponRepository;
import com.ecommerce.ecommerce_backend.repository.UserCouponAttemptRepository;
import com.ecommerce.ecommerce_backend.repository.UserCouponRepository;
import com.ecommerce.ecommerce_backend.repository.UserRepository;

import lombok.*;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponAttemptRepository repo;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
private final UserCouponRepository userCouponRepository;
    
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
User user = userRepository.findByEmail(email)
        .orElseThrow();

Coupon coupon = couponRepository.findRandomActiveCoupon();

if(coupon == null){
    throw new RuntimeException("No coupons available");
}

// ❌ Prevent duplicate coupon
if(userCouponRepository.existsByUserAndCoupon(user, coupon)){
    throw new RuntimeException("Coupon already won, try again");
}

// ✅ SAVE COUPON FOR USER
UserCoupon uc = UserCoupon.builder()
        .user(user)
        .coupon(coupon)
        .used(false)
        .assignedAt(LocalDateTime.now())
        .build();

userCouponRepository.save(uc);

return coupon;}
public List<UserCoupon> getMyCoupons(String email){

    User user = userRepository.findByEmail(email)
            .orElseThrow();

    return userCouponRepository.findByUser(user);
}}