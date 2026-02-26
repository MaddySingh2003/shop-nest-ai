package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.service.CouponService;
import com.ecommerce.ecommerce_backend.service.UserCouponService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final UserCouponService userCouponService;

    // ================= ADMIN =================
    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> create(@RequestBody Coupon coupon){

        if(coupon.getActive() == null){
            coupon.setActive(true);
        }

        coupon.setUsedCount(0);

        return ResponseEntity.ok(couponService.create(coupon));
    }

    // ✅ THIS MUST BE USER ACCESSIBLE ALSO
    @GetMapping("/validate/{code}")
    public ResponseEntity<Coupon> validate(@PathVariable String code){
        return ResponseEntity.ok(couponService.validateCoupon(code));
    }

    // ================= USER =================
    @GetMapping("/gift")
    public ResponseEntity<?> tryCoupon(){

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString(); // ✅ FIXED

        return ResponseEntity.ok(userCouponService.tryLuck(email));
    }
}