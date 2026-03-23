package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.model.Wishlist;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;
import com.ecommerce.ecommerce_backend.repository.UserRepository;
import com.ecommerce.ecommerce_backend.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Wishlist getWishlist(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist w = new Wishlist();
                    w.setUser(user);
                    w.setProducts(new ArrayList<>());
                    return wishlistRepository.save(w);
                });
    }

    public Wishlist addToWishlist(String email, Long productId){

        Wishlist wishlist = getWishlist(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if(!wishlist.getProducts().contains(product)){
            wishlist.getProducts().add(product);
        }

        return wishlistRepository.save(wishlist);
    }

    public Wishlist removeFromWishlist(String email, Long productId){

        Wishlist wishlist = getWishlist(email);

        wishlist.getProducts().removeIf(p -> p.getId().equals(productId));

        return wishlistRepository.save(wishlist);
    }
}
