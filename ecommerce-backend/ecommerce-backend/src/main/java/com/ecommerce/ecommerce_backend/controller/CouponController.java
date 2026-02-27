package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.service.CouponService;
import com.ecommerce.ecommerce_backend.service.UserCouponService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final UserCouponService userCouponService;

    // ================= ADMIN =================
    @PostMapping("/admin/coupon/create")
    public ResponseEntity<Coupon> create(@RequestBody Coupon coupon){
        coupon.setUsedCount(0);
        coupon.setActive(true);
        return ResponseEntity.ok(couponService.create(coupon));
    }

    @GetMapping("/admin/coupon/validate/{code}")
    public ResponseEntity<Coupon> validate(@PathVariable String code){
        return ResponseEntity.ok(couponService.validateCoupon(code));
    }

    // ================= USER =================
    @GetMapping("/coupon/gift")
    public ResponseEntity<?> tryCoupon(){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName(); // ✅ FIXED

        return ResponseEntity.ok(userCouponService.tryLuck(email));
    }
}