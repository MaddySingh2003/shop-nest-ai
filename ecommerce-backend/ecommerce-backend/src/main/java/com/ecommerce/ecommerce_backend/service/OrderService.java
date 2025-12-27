package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.*;
import com.ecommerce.ecommerce_backend.model.Order.Status;
import com.ecommerce.ecommerce_backend.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public Order placeOrder(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if(cart.getItems().isEmpty()){
            throw new RuntimeException("No items in cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);

        var items = cart.getItems().stream().map(c ->
                OrderItem.builder()
                        .order(order)
                        .productId(c.getProduct().getId())
                        .productName(c.getProduct().getName())
                        .price(c.getProduct().getPrice())
                        .quantity(c.getQuantity())
                        .build()
        ).collect(Collectors.toList());

        order.setItems(items);

        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        cartRepository.delete(cart);   // clear cart after order

        return saved;
    }

    public java.util.List<Order> getUserOrders(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow();
        return orderRepository.findByUser(user);
    }

    public java.util.List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderByIdForUser(Long orderId, String email) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (!order.getUser().getEmail().equals(email)) {
        throw new RuntimeException("You are not allowed to view this order");
    }

    return order;
}

public Order getOrder(Long id){
    return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
}


    @Transactional
public Order updateOrderStatus(Long orderId, Status status) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus(status);
    return orderRepository.save(order);
}

@Transactional
public Order cancelOrder(Long orderId, String email) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (!order.getUser().getEmail().equals(email)) {
        throw new RuntimeException("You cannot cancel this order");
    }

    if (order.getStatus() == Status.SHIPPED ||
        order.getStatus() == Status.DELIVERED) {
        throw new RuntimeException("Order cannot be cancelled now");
    }

    order.setStatus(Status.CANCELLED);
    return orderRepository.save(order);
}


}
