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
    private final ProductRepository productRepository;

@Transactional
public Order placeOrder(String email, Long addressId, String couponCode) {

    // 1️⃣ USER
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 2️⃣ CART
    Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

    if (cart.getItems().isEmpty()) {
        throw new RuntimeException("Cart is empty");
    }

    // 3️⃣ ADDRESS
    Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new RuntimeException("Address not found"));

    if (!address.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("You cannot use this address");
    }

    // 4️⃣ STOCK CHECK (BEFORE ORDER)
    for (CartItem ci : cart.getItems()) {
        if (ci.getProduct().getStock() < ci.getQuantity()) {
            throw new RuntimeException(
                    "Out of stock: " + ci.getProduct().getName()
            );
        }
    }

    // 5️⃣ CREATE ORDER
    Order order = new Order();
    order.setUser(user);
    order.setStatus(Order.Status.PENDING);
    order.setOrderDate(LocalDateTime.now());

    double total = 0;
    List<OrderItem> orderItems = new ArrayList<>();

    // 6️⃣ CREATE ORDER ITEMS + REDUCE STOCK
    for (CartItem ci : cart.getItems()) {

        Product product = ci.getProduct();

        OrderItem oi = new OrderItem();
        oi.setOrder(order);
        oi.setProductId(product.getId());
        oi.setProductName(product.getName());
        oi.setPrice(product.getPrice());
        oi.setQuantity(ci.getQuantity());

        total += product.getPrice() * ci.getQuantity();
        orderItems.add(oi);

        // 🔥 reduce stock
        product.setStock(product.getStock() - ci.getQuantity());
        productRepository.save(product);
    }

    // 7️⃣ APPLY COUPON (SAFE)
    Coupon appliedCoupon = null;

    if (couponCode != null && !couponCode.isBlank()) {

        Coupon coupon = couponService.validateCoupon(couponCode);

        double discount = total * (coupon.getDiscountPercent() / 100.0);

        if (discount > coupon.getMaxDiscount()) {
            discount = coupon.getMaxDiscount();
        }

        total -= discount;
        appliedCoupon = coupon;
    }

    // 8️⃣ SET TOTAL & ITEMS
    order.setTotalAmount(total);
    order.setItems(orderItems);

    // 9️⃣ COPY ADDRESS SNAPSHOT
    order.setShippingName(address.getFullName());
    order.setShippingPhone(address.getPhone());
    order.setShippingStreet(address.getStreet());
    order.setShippingCity(address.getCity());
    order.setShippingState(address.getState());
    order.setShippingZip(address.getZipCode());
    order.setShippingCountry(address.getCountry());

    // 🔟 SAVE ORDER
    Order savedOrder = orderRepository.save(order);

    // 1️⃣1️⃣ MARK COUPON USED (AFTER SUCCESS)
    if (appliedCoupon != null) {
        couponService.markUsed(appliedCoupon);
    }

    // 1️⃣2️⃣ CLEAR CART
    cart.getItems().clear();
    cartRepository.save(cart);

    return savedOrder;
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
    for(OrderItem item : order.getItems()){
    var product = productRepository.findById(item.getProductId())
            .orElseThrow();

    product.setStock(product.getStock() + item.getQuantity());
    productRepository.save(product);
}

    return orderRepository.save(order);
}



}
