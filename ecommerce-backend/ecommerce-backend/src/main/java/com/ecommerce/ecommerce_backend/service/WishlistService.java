package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.*;
import com.ecommerce.ecommerce_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private Wishlist getUserWishlist(String email){
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user)
                .orElseGet(() -> wishlistRepository.save(
                        Wishlist.builder()
                                .user(user)
                                .products(new java.util.ArrayList<>())
                                .build()
                ));
    }

    public Wishlist add(String email, Long productId){
        var wishlist = getUserWishlist(email);
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if(!wishlist.getProducts().contains(product)){
            wishlist.getProducts().add(product);
        }

        return wishlistRepository.save(wishlist);
    }

    public Wishlist remove(String email, Long productId){
        var wishlist = getUserWishlist(email);

        wishlist.getProducts().removeIf(p -> p.getId().equals(productId));

        return wishlistRepository.save(wishlist);
    }

    public Wishlist getMyWishlist(String email){
        return getUserWishlist(email);
    }
}
