package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Order;
import com.ecommerce.ecommerce_backend.model.Payment;
import com.ecommerce.ecommerce_backend.repository.OrderRepository;
import com.ecommerce.ecommerce_backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

    public Payment initiatePayment(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .status(Payment.Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return paymentRepo.save(payment);
    }

    public Payment confirmPayment(Long paymentId, boolean success){

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if(success){
            payment.setStatus(Payment.Status.SUCCESS);
            payment.getOrder().setStatus(Order.Status.CONFIRMED);
        } else {
            payment.setStatus(Payment.Status.FAILED);
            payment.getOrder().setStatus(Order.Status.CANCELLED);
        }

        orderRepo.save(payment.getOrder());
        return paymentRepo.save(payment);
    }
}
