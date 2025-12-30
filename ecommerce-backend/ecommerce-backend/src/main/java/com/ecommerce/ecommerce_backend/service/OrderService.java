package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.*;
import com.ecommerce.ecommerce_backend.model.Order.Status;
import com.ecommerce.ecommerce_backend.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CouponService couponService;

    
public Order placeOrder(String email, Long addressId,String couponCode){

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

    if(cart.getItems().isEmpty()){
        throw new RuntimeException("Cart is empty");
    }

    Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new RuntimeException("Address not found"));

    if(!address.getUser().equals(user)){
        throw new RuntimeException("You cannot use this address");
    }

    Order order = new Order();
    order.setUser(user);
    order.setStatus(Order.Status.PENDING);
    order.setOrderDate(LocalDateTime.now());

    double total = 0;

    List<OrderItem> items = new ArrayList<>();

    for(CartItem ci : cart.getItems()){
        OrderItem oi = new OrderItem();
        oi.setOrder(order);
        oi.setProductId(ci.getProduct().getId());
        oi.setProductName(ci.getProduct().getName());
        oi.setPrice(ci.getProduct().getPrice());
        oi.setQuantity(ci.getQuantity());

        total += ci.getProduct().getPrice() * ci.getQuantity();
        items.add(oi);
    }
// Apply coupon if present
if(couponCode != null && !couponCode.isBlank()) {

    Coupon coupon = couponService.validateCoupon(couponCode);

    double discount = total * (coupon.getDiscountPercent() / 100.0);

    if(discount > coupon.getMaxDiscount()){
        discount = coupon.getMaxDiscount();
    }

    total -= discount;

    couponService.markUsed(coupon);

    System.out.println("Coupon applied: " + couponCode + " Discount: " + discount);
}

order.setTotalAmount(total);
order.setItems(items);


    // copy address snapshot
    order.setShippingName(address.getFullName());
    order.setShippingPhone(address.getPhone());
    order.setShippingStreet(address.getStreet());
    order.setShippingCity(address.getCity());
    order.setShippingState(address.getState());
    order.setShippingZip(address.getZipCode());
    order.setShippingCountry(address.getCountry());

    Order saved = orderRepository.save(order);

    cart.getItems().clear();
    cartRepository.save(cart);

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
