package com.example.fotori.service.impl;

import com.example.fotori.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.backend-url}")
    private String backendUrl;

    @Override
    @Async
    public void sendVerificationEmail(String email, String token) {

        String verifyUrl =
            backendUrl + "/auth/verify-email?token=" + token;

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
    @Async
    public void sendResetPasswordEmail(String email, String token) {

        String resetUrl =
            frontendUrl + "/reset-password?token=" + token;

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
