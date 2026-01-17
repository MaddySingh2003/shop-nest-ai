package com.ecommerce.ecommerce_backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_backend.repository.UserRepository;
@RestController
@RequestMapping("/user")
public class SecureController {

@Autowired
private UserRepository userRepository;

@GetMapping("/me")
public ResponseEntity<?>getProfile(){
     String email=(String) SecurityContextHolder
     .getContext()
     .getAuthentication()
     .getPrincipal();

      return ResponseEntity.ok(
        userRepository.findByEmail(email)
            .orElseThrow()
        );
      
}


    @GetMapping("/test")
    public ResponseEntity<?> userTest() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
                "principal", auth.getPrincipal(),
                "authorities", auth.getAuthorities()
        ));
    }
   

}
