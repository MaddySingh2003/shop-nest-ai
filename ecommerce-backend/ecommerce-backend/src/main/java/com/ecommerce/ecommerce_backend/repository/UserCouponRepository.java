package com.ecommerce.ecommerce_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_backend.model.Coupon;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.model.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    Optional<UserCoupon> findByUserAndCoupon_Code(User user, String code);

    boolean existsByUserAndCoupon(User user, Coupon coupon);

    List<UserCoupon> findByUser(User user);
}
