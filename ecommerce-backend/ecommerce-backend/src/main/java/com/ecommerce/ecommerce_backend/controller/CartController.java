package com.ecommerce.ecommerce_backend.controller;

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

    private String getLoggedUser(){
        return (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(){
        return ResponseEntity.ok(cartService.getUserCart(getLoggedUser()));
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long productId,
            @RequestParam int qty){
        return ResponseEntity.ok(cartService.addToCart(getLoggedUser(), productId, qty));
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<Cart> updateQty(
            @PathVariable Long itemId,
            @RequestParam int qty){
        return ResponseEntity.ok(cartService.updateQuantity(getLoggedUser(), itemId, qty));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long itemId){
        return ResponseEntity.ok(cartService.removeItem(getLoggedUser(), itemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Cart> clear(){
        return ResponseEntity.ok(cartService.clearCart(getLoggedUser()));
    }
}
