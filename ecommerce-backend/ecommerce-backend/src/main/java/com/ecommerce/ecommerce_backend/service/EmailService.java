package com.ecommerce.ecommerce_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    public void sendVerificationEmail(String to,String token){
        String link= "http://localhost:8080/auth/verify?token=" + token;

        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("noreply@shopnest.com");
        message.setTo(to);
        message.setSubject("Verify your account");
        message.setText("Click the link to verify you email:\n"+ link);

        mailSender.send(message);
    }

}
