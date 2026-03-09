package com.example.fotori.service.impl;

import com.example.fotori.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String token) {

        String verifyUrl =
            "http://localhost:3000/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your email");
        message.setText(
            "Click to verify your account:\n" + verifyUrl
        );

        mailSender.send(message);
    }

    @Override
    public void sendResetPasswordEmail(String email, String token) {

        String resetUrl =
            "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset your password");
        message.setText(
            "Click the link below to reset your password:\n" + resetUrl
        );

        mailSender.send(message);
    }
}
