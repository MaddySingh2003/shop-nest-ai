package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.LoginRequest;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.model.VerificationToken;
import com.ecommerce.ecommerce_backend.repository.UserRepository;
import com.ecommerce.ecommerce_backend.repository.VerificationTokenRepository;
import com.ecommerce.ecommerce_backend.service.JwtService;
import com.ecommerce.ecommerce_backend.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final VerificationTokenRepository tokenRepository;

   

    // ---------------- REGISTER ----------------
@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody User user) {

    try {

        userService.register(user);

        return ResponseEntity.ok(
            Map.of("message",
            "Registration successful. Please verify your email.")
        );

    } catch (RuntimeException ex) {

        return ResponseEntity.badRequest().body(ex.getMessage());

    }
}

@GetMapping("/verify")
public ResponseEntity<?> verifyEmail(@RequestParam String token){

    VerificationToken verificationToken =
            tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));

    if(verificationToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())){
        return ResponseEntity.badRequest().body("Token expired");
    }

    User user = verificationToken.getUser();

    if(user.getEnabled()){
        return ResponseEntity.ok("Email already verified");
    }

    user.setEnabled(true);
    userRepository.save(user);

    return ResponseEntity.ok("Email verified successfully");
}

@GetMapping("/secure")
public ResponseEntity<String> secured(){
    return ResponseEntity.ok("You are authorized 🎉");
}

@GetMapping("/users")
public Object testUsers() {
    return userRepository.findAll();
}

//             _____________________-------------------  
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request){

    var userOpt = userRepository.findByEmail(request.getEmail());

    if(userOpt.isEmpty()){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not found");
    }

    var user = userOpt.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid Credentials");
    }
    if(Boolean.FALSE.equals(user.getEnabled())){
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Please verify your email before login");
}

    String token = jwtService.generateToken(user.getEmail());
    var expiry = jwtService.getExpiry(token);

    return ResponseEntity.ok(Map.of(
            "token", token,
            "email", user.getEmail(),
            "role", user.getRole().name(),
            "expiresAt", expiry.getTime()
    ));
}


}
