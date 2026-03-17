package com.example.fotori.service.impl;

import com.example.fotori.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    @Override
    public void sendVerificationEmail(String email, String token) {

        String verifyUrl =
            "https://fotori-production.up.railway.app/api/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        if (fromAddress != null && !fromAddress.isBlank()) {
            message.setFrom(fromAddress);
        }
        message.setSubject("Verify your email");
        message.setText(
            "Click to verify your account:\n" + verifyUrl
        );

        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send verification email to {}", email, e);
            throw e;
        }
    }

    @Override
    public void sendResetPasswordEmail(String email, String token) {

        String resetUrl =
            "http://localhost:3001/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        if (fromAddress != null && !fromAddress.isBlank()) {
            message.setFrom(fromAddress);
        }
        message.setSubject("Reset your password");
        message.setText(
            "Click the link below to reset your password:\n" + resetUrl
        );

        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send reset password email to {}", email, e);
            throw e;
        }
    }
}
