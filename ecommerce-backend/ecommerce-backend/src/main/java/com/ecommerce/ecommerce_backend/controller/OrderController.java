package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.OrderItemResponse;
import com.ecommerce.ecommerce_backend.dto.OrderResponse;
import com.ecommerce.ecommerce_backend.model.Order;
import com.ecommerce.ecommerce_backend.model.Order.Status;
import com.ecommerce.ecommerce_backend.service.InvoiceService;
import com.ecommerce.ecommerce_backend.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final InvoiceService invoiceService;

    // ===============================
    // PLACE ORDER
    // ===============================
    @PostMapping("/place/{addressId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(
            @PathVariable Long addressId,
            @RequestParam(required = false) String coupon
    ) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Order order = orderService.placeOrder(email, addressId, coupon);
  

        return ResponseEntity.ok(mapToResponse(order));
    }

    // ===============================
    // CONFIRM AFTER PAYMENT
    // ===============================
   @PutMapping("/confirm/{orderId}")
@PreAuthorize("hasRole('USER')")
public ResponseEntity<Order> confirmAfterPayment(
        @PathVariable Long orderId,
        @RequestParam String method){

    String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    return ResponseEntity.ok(
        orderService.confirmOrderAfterPayment(orderId,email,method)
    );
}

    // ===============================
    // DOWNLOAD INVOICE
    // ===============================
    @GetMapping("/invoice/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long orderId) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        byte[] pdf = invoiceService.generateInvoice(orderId, email);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition",
                        "attachment; filename=invoice-" + orderId + ".pdf")
                .body(pdf);
    }

    // ===============================
    // UPDATE STATUS (ADMIN)
    // ===============================
    @PutMapping("/update/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long orderId,
            @RequestParam Status status
    ) {
        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, status)
        );
    }

    // ===============================
    // CANCEL ORDER
    // ===============================
    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(
                orderService.cancelOrder(orderId, email)
        );
    }

    // ===============================
    // GET SINGLE ORDER
    // ===============================
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(
                orderService.getOrderByIdForUser(orderId, email)
        );
    }

    // ===============================
    // GET USER ORDERS
    // ===============================
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public Page<Order> myOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return orderService.getUserOrders(email, page, size);
    }

    // ===============================
    // ADMIN — ALL ORDERS
    // ===============================
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Order> allOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.getAllOrders(page, size);
    }

    // ===============================
    // ADMIN — GET ORDER BY ID
    // ===============================
    @GetMapping("/admin/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> getOrderForAdmin(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    // ===============================
    // MAPPER
    // ===============================
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .shippingName(order.getShippingName())
                .shippingCity(order.getShippingCity())
                .shippingState(order.getShippingState())
                .shippingCountry(order.getShippingCountry())
                .items(
                        order.getItems().stream()
                                .map(i -> OrderItemResponse.builder()
                                        .productId(i.getProductId())
                                        .productName(i.getProductName())
                                        .price(i.getPrice())
                                        .quantity(i.getQuantity())
                                        .build()
                                ).toList()
                )
                .build();
    }

    @DeleteMapping("/admin/delete/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    orderService.softDeleteOrder(id);
    return ResponseEntity.ok().build();
}

}
