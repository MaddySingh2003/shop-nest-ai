package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.PaymentResponse;
import com.ecommerce.ecommerce_backend.model.Payment;
import com.ecommerce.ecommerce_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

   @PostMapping("/initiate/{orderId}")
public ResponseEntity<PaymentResponse> initiate(@PathVariable Long orderId){

    Payment p = paymentService.initiatePayment(orderId);

    return ResponseEntity.ok(
            PaymentResponse.builder()
                    .id(p.getId())
                    .amount(p.getAmount())
                    .status(p.getStatus().name())
                    .build()
    );
}

@PostMapping("/confirm/{paymentId}")
public ResponseEntity<PaymentResponse> confirm(
        @PathVariable Long paymentId,
        @RequestParam boolean success){

    Payment p = paymentService.confirmPayment(paymentId, success);

    return ResponseEntity.ok(
            PaymentResponse.builder()
                    .id(p.getId())
                    .amount(p.getAmount())
                    .status(p.getStatus().name())
                    .build()
    );
}

}
