package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.dto.CartItemResponse;
import com.ecommerce.ecommerce_backend.dto.CartResponse;
import com.ecommerce.ecommerce_backend.model.*;
import com.ecommerce.ecommerce_backend.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
public Cart getUserCart(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return cartRepository.findByUser(user)
            .orElseGet(() -> cartRepository.save(
                    Cart.builder().user(user).items(new ArrayList<>()).build()
            ));
}


    public Cart addToCart(String email, Long productId, int qty){
        Cart cart = getUserCart(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        var items = cart.getItems();

        var existing = items.stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if(existing.isPresent()){
            existing.get().setQuantity(existing.get().getQuantity() + qty);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(qty)
                    .build();
            items.add(item);
        }

        return cartRepository.save(cart);
    }

    public Cart updateQuantity(String email, Long itemId, int qty){
        Cart cart = getUserCart(email);

        cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"))
                .setQuantity(qty);

        return cartRepository.save(cart);
    }

    public Cart removeItem(String email, Long itemId){
        Cart cart = getUserCart(email);

        cart.getItems().removeIf(i -> i.getId().equals(itemId));

        return cartRepository.save(cart);
    }

    public Cart clearCart(String email){
        Cart cart = getUserCart(email);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
    public CartResponse getCartResponse(String email) {

    Cart cart = getUserCart(email);

    List<CartItemResponse> items = cart.getItems().stream()
            .map(item -> CartItemResponse.builder()
                    .itemId(item.getId())
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .price(item.getProduct().getPrice())
                    .quantity(item.getQuantity())
                    .stock(item.getProduct().getStock()) // ✅ IMPORTANT
                    .build()
            ).toList();

    return CartResponse.builder()
            .id(cart.getId())
            .items(items)
            .build();
}
}
