package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.*;
import com.ecommerce.ecommerce_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Review addReview(String email, Long productId, int rating, String comment){

        if(rating < 1 || rating > 5)
            throw new RuntimeException("Rating must be between 1–5");

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Prevent duplicate review from same user
        if(reviewRepository.findByProductIdAndUserEmail(productId, email).isPresent())
            throw new RuntimeException("You already reviewed this product");

        var review = Review.builder()
                .rating(rating)
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .product(product)
                .user(user)
                .build();

        Review saved = reviewRepository.save(review);

        updateProductRating(product);
        return saved;
    }


    public Review updateReview(String email, Long productId, int rating, String comment){
        var review = reviewRepository.findByProductIdAndUserEmail(productId, email)
                .orElseThrow(() -> new RuntimeException("No review to update"));

        review.setRating(rating);
        review.setComment(comment);
        review.setUpdatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        updateProductRating(review.getProduct());
        return saved;
    }


    public void deleteReview(String email, Long reviewId){
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if(!review.getUser().getEmail().equals(email))
            throw new RuntimeException("You cannot delete others review");

        var product = review.getProduct();
        reviewRepository.delete(review);
        updateProductRating(product);
    }


    public List<Review> getProductReviews(Long productId){
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return reviewRepository.findByProduct(product);
    }


    private void updateProductRating(Product product){
        var reviews = reviewRepository.findByProduct(product);

        double avg = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);

        product.setPrice(avg); // If you have rating field use it
        productRepository.save(product);
    }
}
