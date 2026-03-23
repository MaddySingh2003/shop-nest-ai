package com.ecommerce.ecommerce_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_backend.dto.LoginRequest;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.model.User.Role;
import com.ecommerce.ecommerce_backend.model.VerificationToken;
import com.ecommerce.ecommerce_backend.repository.UserRepository;
import com.ecommerce.ecommerce_backend.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;


   
 public User register(User user) {

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new RuntimeException("Email already registered");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setEnabled(false);

    if (userRepository.count() == 0) {
        user.setRole(Role.ADMIN);
    } else {
        user.setRole(Role.USER);
    }

    User savedUser = userRepository.save(user);

    String token = java.util.UUID.randomUUID().toString();

    VerificationToken verificationToken = VerificationToken.builder()
            .token(token)
            .user(savedUser)
            .expiryDate(java.time.LocalDateTime.now().plusHours(24))
            .build();

    tokenRepository.save(verificationToken);

    try{
        emailService.sendVerificationEmail(savedUser.getEmail(), token);
    }catch(Exception e){
        System.out.println("Email failed: " + e.getMessage());
    }

    return savedUser;
}
    public void authenticate(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ THIS IS REQUIRED
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return "Login successful";
    }
}
