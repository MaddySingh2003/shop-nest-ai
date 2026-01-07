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

    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .items(
                        cart.getItems().stream()
                                .map(i -> CartItemResponse.builder()
                                        .itemId(i.getId())
                                        .productId(i.getProduct().getId())
                                        .productName(i.getProduct().getName())
                                        .price(i.getProduct().getPrice())
                                        .quantity(i.getQuantity())
                                        .build()
                                ).toList()
                )
                .build();
    }

    @GetMapping("/my")
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(
                toResponse(cartService.getUserCart(getEmail()))
        );
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long productId,
            @RequestParam int qty
    ) {
        return ResponseEntity.ok(
                toResponse(cartService.addToCart(getEmail(), productId, qty))
        );
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<CartResponse> updateQty(
            @PathVariable Long itemId,
            @RequestParam int qty
    ) {
        return ResponseEntity.ok(
                toResponse(cartService.updateQuantity(getEmail(), itemId, qty))
        );
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(
                toResponse(cartService.removeItem(getEmail(), itemId))
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartResponse> clearCart() {
        return ResponseEntity.ok(
                toResponse(cartService.clearCart(getEmail()))
        );
    }
}
