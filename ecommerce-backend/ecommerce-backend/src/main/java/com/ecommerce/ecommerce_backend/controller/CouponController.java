package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // ---------------- CREATE COUPON (ADMIN) ----------------
    @PostMapping("/create")
    public ResponseEntity<Coupon> create(@RequestBody Coupon coupon){
        coupon.setUsedCount(0);
        coupon.setActive(true);
        return ResponseEntity.ok(couponService.create(coupon));
    }

    // ---------------- GET COUPON (ADMIN) ----------------
    @GetMapping("/{code}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable String code){
        return ResponseEntity.ok(couponService.getByCode(code));
    }

    // ---------------- LIST ALL (ADMIN OPTIONAL) ----------------
    // enable if needed
    // @GetMapping("/all")
    // public List<Coupon> all(){
    //     return repo.findAll();
    // }

    // ---------------- DISABLE COUPON (ADMIN) ----------------
    @PutMapping("/disable/{code}")
    public ResponseEntity<?> disable(@PathVariable String code){
        Coupon coupon = couponService.getByCode(code);
        coupon.setActive(false);
        return ResponseEntity.ok("Coupon disabled");
    }

    // ---------------- ENABLE COUPON (ADMIN) ----------------
    @PutMapping("/enable/{code}")
    public ResponseEntity<?> enable(@PathVariable String code){
        Coupon coupon = couponService.getByCode(code);
        coupon.setActive(true);
        return ResponseEntity.ok("Coupon enabled");
    }
}
