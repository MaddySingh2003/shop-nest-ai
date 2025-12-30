package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Review;
import com.ecommerce.ecommerce_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private String getEmail(){
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal().toString();
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<Review> add(
            @PathVariable Long productId,
            @RequestBody Map<String,Object> body){

        int rating = (int) body.get("rating");
        String comment = (String) body.get("comment");

        return ResponseEntity.ok(
                reviewService.addReview(getEmail(), productId, rating, comment)
        );
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Review> update(
            @PathVariable Long productId,
            @RequestBody Map<String,Object> body){

        return ResponseEntity.ok(
                reviewService.updateReview(
                        getEmail(),
                        productId,
                        (int) body.get("rating"),
                        (String) body.get("comment"))
        );
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId){
        reviewService.deleteReview(getEmail(), reviewId);
        return ResponseEntity.ok("Review deleted");
    }

    @GetMapping("/product/{productId}")
    public List<Review> reviews(@PathVariable Long productId){
        return reviewService.getProductReviews(productId);
    }
}
