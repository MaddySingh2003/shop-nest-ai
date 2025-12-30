package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.OrderItemResponse;
import com.ecommerce.ecommerce_backend.dto.OrderResponse;
import com.ecommerce.ecommerce_backend.model.Order;
import com.ecommerce.ecommerce_backend.model.Order.Status;
import com.ecommerce.ecommerce_backend.service.*;
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
    private final InvoiceService invoiceService;

  @PostMapping("/place/{addressId}")
public ResponseEntity<OrderResponse> placeOrder(
        @PathVariable Long addressId,
        @RequestParam(required = false) String coupon
) {

    String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal()
            .toString();

    Order order = orderService.placeOrder(email, addressId, coupon);

    OrderResponse response = OrderResponse.builder()
            .id(order.getId())
            .status(order.getStatus().name())
            .totalAmount(order.getTotalAmount())
            .orderDate(order.getOrderDate())
            .shippingName(order.getShippingName())
            .shippingCity(order.getShippingCity())
            .shippingState(order.getShippingState())
            .shippingCountry(order.getShippingCountry())
            .items(
                    order.getItems().stream().map(item ->
                            OrderItemResponse.builder()
                                    .productId(item.getProductId())
                                    .productName(item.getProductName())
                                    .price(item.getPrice())
                                    .quantity(item.getQuantity())
                                    .build()
                    ).toList()
            )
            .build();

    return ResponseEntity.ok(response);
}


@GetMapping("/invoice/{orderId}")
public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long orderId) {

    String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal()
            .toString();

    byte[] pdf = invoiceService.generateInvoice(orderId, email);

    return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .header("Content-Disposition", "attachment; filename=invoice-" + orderId + ".pdf")
            .body(pdf);
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
