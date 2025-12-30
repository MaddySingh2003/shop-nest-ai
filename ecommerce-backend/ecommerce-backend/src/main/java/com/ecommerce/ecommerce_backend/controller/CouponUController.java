package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponUController {

    private final CouponService couponService;

    @GetMapping("/validate/{code}")
    public ResponseEntity<Coupon> validate(@PathVariable String code){
        return ResponseEntity.ok(couponService.validateCoupon(code));
    }
}
