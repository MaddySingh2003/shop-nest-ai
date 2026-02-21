package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.CartItemResponse;
import com.ecommerce.ecommerce_backend.dto.CartResponse;
import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private String getEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();
    }

    @GetMapping("/my")
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(
                cartService.getCartResponse(getEmail())
        );
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long productId,
            @RequestParam int qty
    ) {
        cartService.addToCart(getEmail(), productId, qty);

        return ResponseEntity.ok(
                cartService.getCartResponse(getEmail())
        );
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<CartResponse> updateQty(
            @PathVariable Long itemId,
            @RequestParam int qty
    ) {
        cartService.updateQuantity(getEmail(), itemId, qty);

        return ResponseEntity.ok(
                cartService.getCartResponse(getEmail())
        );
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable Long itemId
    ) {
        cartService.removeItem(getEmail(), itemId);

        return ResponseEntity.ok(
                cartService.getCartResponse(getEmail())
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartResponse> clearCart() {
        cartService.clearCart(getEmail());

        return ResponseEntity.ok(
                cartService.getCartResponse(getEmail())
        );
    }
}