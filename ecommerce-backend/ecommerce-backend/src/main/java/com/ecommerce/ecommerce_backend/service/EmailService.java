package com.ecommerce.ecommerce_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Async
public void sendVerificationEmail(String to, String token) {

    try {

     String link = "http://localhost:8080/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("ShopNest <milansuryavanshi27@gmail.com>");
        message.setTo(to);
        message.setSubject("Verify your account");
        message.setText(
                "Welcome to ShopNest!\n\n" +
                "Click the link below to verify your email:\n" +
                link
        );

        mailSender.send(message);

        System.out.println("Email sent successfully");

    } catch (Exception e) {

        System.out.println("Email failed: " + e.getMessage());
        e.printStackTrace();
    }
}
}