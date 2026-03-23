package com.ecommerce.ecommerce_backend.repository;

import com.ecommerce.ecommerce_backend.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String code);

    @Query(value = """
        SELECT * FROM coupon 
        WHERE active = true 
        AND expiry_date > NOW()
        ORDER BY RANDOM() 
        LIMIT 1
    """, nativeQuery = true)
    Coupon findRandomActiveCoupon();
}
