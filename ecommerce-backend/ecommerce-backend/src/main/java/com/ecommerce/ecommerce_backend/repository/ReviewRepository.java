package com.ecommerce.ecommerce_backend.repository;

import com.ecommerce.ecommerce_backend.model.Review;
import com.ecommerce.ecommerce_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    Optional<Review> findByProductIdAndUserEmail(Long productId, String email);
}
