package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.repository.CouponRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository repo;

    public Coupon validateCoupon(String code){

        Coupon coupon = repo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Coupon does not exist"));

        if(Boolean.FALSE.equals(coupon.getActive()))
            throw new RuntimeException("Coupon disabled");

        if(coupon.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Coupon expired");

        if(coupon.getUsedCount() >= coupon.getUsageLimit())
            throw new RuntimeException("Coupon usage limit reached");

        return coupon;
    }

    public void markUsed(Coupon coupon){
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        repo.save(coupon);
    }

    public Coupon create(Coupon coupon){
        return repo.save(coupon);
    }
}