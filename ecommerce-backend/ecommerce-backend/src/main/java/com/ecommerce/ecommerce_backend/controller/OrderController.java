package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Order;
import com.ecommerce.ecommerce_backend.model.Order.Status;
import com.ecommerce.ecommerce_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    
 @PostMapping("/place/{addressId}")
public ResponseEntity<Order> placeOrder(
        @PathVariable Long addressId,
        @RequestParam(required = false) String coupon){

    String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();

    return ResponseEntity.ok(orderService.placeOrder(email, addressId, coupon));
}



@PutMapping("/update/{orderId}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> updateStatus(
        @PathVariable Long orderId,
        @RequestParam Status status) {

    return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
}

@PutMapping("/cancel/{orderId}")
@PreAuthorize("hasRole('USER')")
public ResponseEntity<?> cancelOrder(@PathVariable Long orderId,
                                     Authentication authentication) {

    String email = authentication.getName();
    return ResponseEntity.ok(orderService.cancelOrder(orderId, email));
}
@GetMapping("/{orderId}")
public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {

    String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal()
            .toString();

    return ResponseEntity.ok(orderService.getOrderByIdForUser(orderId, email));
}


@GetMapping("/{orderId}/admin")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> getOrderForAdmin(@PathVariable Long orderId) {
    return ResponseEntity.ok(orderService.getOrder(orderId));
}


    @GetMapping("/my")
    public List<Order> myOrders(){
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal().toString();

        return orderService.getUserOrders(email);
    }

    // ADMIN — ALL ORDERS
    @GetMapping("/all")
    public List<Order> all(){
        return orderService.getAllOrders();
    }
}
