package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Wishlist;
import com.ecommerce.ecommerce_backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    private String getEmail(){
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();
    }

    @GetMapping("/my")
    public Wishlist myWishlist(){
        return wishlistService.getWishlist(getEmail());
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> add(@PathVariable Long productId){
        return ResponseEntity.ok(
                wishlistService.addToWishlist(getEmail(), productId)
        );
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> remove(@PathVariable Long productId){
        return ResponseEntity.ok(
                wishlistService.removeFromWishlist(getEmail(), productId)
        );
    }
}
