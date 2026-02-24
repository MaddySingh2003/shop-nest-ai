package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.dto.DashboardStats;
import com.ecommerce.ecommerce_backend.model.*;
import com.ecommerce.ecommerce_backend.model.Order.Status;
import com.ecommerce.ecommerce_backend.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CouponService couponService;
    private final ProductRepository productRepository;

    // =========================
    // PLACE ORDER
    // =========================
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

        // 4️⃣ STOCK CHECK
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
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
              order.setPaymentStatus("UNPAID");
order.setPaymentMethod("NOT_SELECTED");


        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 6️⃣ CREATE ITEMS + REDUCE STOCK
        for (CartItem ci : cart.getItems()) {

            Product product = ci.getProduct();

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(ci.getQuantity());

            total += product.getPrice() * ci.getQuantity();
            orderItems.add(item);

            // 🔥 Reduce stock
            product.setStock(product.getStock() - ci.getQuantity());
            productRepository.save(product);
        }

        // 7️⃣ APPLY COUPON
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

        // 9️⃣ ADDRESS SNAPSHOT
        order.setShippingName(address.getFullName());
        order.setShippingPhone(address.getPhone());
        order.setShippingStreet(address.getStreet());
        order.setShippingCity(address.getCity());
        order.setShippingState(address.getState());
        order.setShippingZip(address.getZipCode());
        order.setShippingCountry(address.getCountry());

        // 🔟 SAVE ORDER
        Order savedOrder = orderRepository.save(order);

        // 1️⃣1️⃣ MARK COUPON USED
        if (appliedCoupon != null) {
            couponService.markUsed(appliedCoupon);
        }

        // 1️⃣2️⃣ CLEAR CART
        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    // =========================
    // USER ORDERS (PAGINATED)
    // =========================
    public Page<Order> getUserOrders(String email, int page, int size) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("orderDate").descending()
        );

        return orderRepository.findByUser(user, pageable);
    }
    @Transactional
public Order confirmOrderAfterPayment(Long orderId, String email, String method){

    Order order = orderRepository.findById(orderId)
        .orElseThrow();

    if(!order.getUser().getEmail().equals(email)){
        throw new RuntimeException("Unauthorized");
    }

    order.setStatus(Status.CONFIRMED);
    order.setPaymentStatus("PAID");
    order.setPaymentMethod(method);

    return orderRepository.save(order);
}


    // =========================
    // ADMIN — ALL ORDERS
    // =========================
    public Page<Order> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("orderDate").descending()
        );
        return orderRepository.findAll(pageable);
    }

    // =========================
    // GET ORDER (USER)
    // =========================
    public Order getOrderByIdForUser(Long orderId, String email) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to view this order");
        }

        return order;
    }

    // =========================
    // GET ORDER (ADMIN)
    // =========================
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // =========================
    // UPDATE STATUS (ADMIN)
    // =========================
    @Transactional
    public Order updateOrderStatus(Long orderId, Status status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    // =========================
    // CANCEL ORDER (USER)
    // =========================
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

        // 🔄 Restore stock
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow();

            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        return orderRepository.save(order);
    }
    @Transactional
public void deleteOrder(Long id){
    orderRepository.deleteById(id);
}

  @Transactional
public void softDeleteOrder(Long id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setDeleted(true);
    orderRepository.save(order);
}
public DashboardStats getStats() {

    long totalOrders = orderRepository.count();

  double totalRevenue = Optional.ofNullable(orderRepository.getTotalRevenue()).orElse(0.0);

    long pending = orderRepository.countByStatus(Status.PENDING);
    long confirmed = orderRepository.countByStatus(Status.CONFIRMED);
    long delivered = orderRepository.countByStatus(Status.DELIVERED);
    

    return DashboardStats.builder()
            .totalOrders(totalOrders)
            .totalRevenue(totalRevenue)
            .pendingOrders(pending)
            .confirmedOrders(confirmed)
            .deliveredOrders(delivered)
            .build();
}
  
}
