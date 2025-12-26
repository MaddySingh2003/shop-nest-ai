package com.ecommerce.ecommerce_backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/user")
public class SecureController {
     @GetMapping("/test")
public ResponseEntity<?> userTest() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return ResponseEntity.ok(Map.of(
            "principal", auth.getPrincipal(),
            "authorities", auth.getAuthorities()
    ));
}

   

}
