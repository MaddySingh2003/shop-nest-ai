package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon validateCoupon(String code){

        var coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Coupon does not exist"));

        if(!coupon.isActive())
            throw new RuntimeException("Coupon is disabled");

        if(coupon.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Coupon expired");

        if(coupon.getUsedCount() >= coupon.getUsageLimit())
            throw new RuntimeException("Coupon usage limit reached");

        return coupon;
    }

    public void markUsed(Coupon coupon){
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);
    }
    
    private final CouponRepository repo;

    public Coupon create(Coupon coupon){
        return repo.save(coupon);
    }

    public Coupon getByCode(String code){
        return repo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));
    }
}
