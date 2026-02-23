package com.ecommerce.ecommerce_backend.controller;



import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_backend.dto.DashboardStats;
import com.ecommerce.ecommerce_backend.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

private final OrderService orderService;
  
  @GetMapping("/test")
      public ResponseEntity<String> adminTest(){
        return ResponseEntity.ok("ADMIN endpoint working");
      }
      @GetMapping("/stats")
public ResponseEntity<DashboardStats> getStats(){
    return ResponseEntity.ok(orderService.getStats());
}
    
}
