package com.ecommerce.ecommerce_backend.repository;

import com.ecommerce.ecommerce_backend.model.Order;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.model.Order.Status;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user, Pageable pageable);

    Page<Order> findByUser(User user, org.springframework.data.domain.Pageable pageable);
    long countByStatus(Status status);
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.paymentStatus='PAID'")
Double getTotalRevenue();
}
