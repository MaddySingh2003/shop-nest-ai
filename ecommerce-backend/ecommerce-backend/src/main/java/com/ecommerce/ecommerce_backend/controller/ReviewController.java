package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Review;
import com.ecommerce.ecommerce_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<Review> addReview(
            @PathVariable Long productId,
            @RequestBody Review review) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return ResponseEntity.ok(
                reviewService.addReview(email, productId, review)
        );
    }

    @GetMapping("/product/{productId}")
    public List<Review> getReviews(@PathVariable Long productId){
        return reviewService.getProductReviews(productId);
    }
}
