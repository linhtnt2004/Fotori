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
        log.info("Preparing to send verification email to {}", email);
        
        String verifyUrl = backendUrl + "/auth/verify-email?token=" + token;
        log.info("Verification URL: {}", verifyUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        if (fromAddress != null && !fromAddress.isBlank()) {
            message.setFrom(fromAddress);
            log.info("Using fromAddress: {}", fromAddress);
        } else {
            log.warn("fromAddress is empty, using default mail configuration");
        }
        message.setSubject("Verify your email");
        message.setText(
            "Click to verify your account:\n" + verifyUrl
        );

        try {
            mailSender.send(message);
            log.info("Verification email sent successfully to {}", email);
        } catch (MailException e) {
            log.error("Failed to send verification email to {}", email, e);
        }
    }

    @Override
    @Async
    public void sendResetPasswordEmail(String email, String token) {
        log.info("Preparing to send reset password email to {}", email);

        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        log.info("Reset URL: {}", resetUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        if (fromAddress != null && !fromAddress.isBlank()) {
            message.setFrom(fromAddress);
            log.info("Using fromAddress: {}", fromAddress);
        }
        message.setSubject("Reset your password");
        message.setText(
            "Click the link below to reset your password:\n" + resetUrl
        );

        try {
            mailSender.send(message);
            log.info("Reset password email sent successfully to {}", email);
        } catch (MailException e) {
            log.error("Failed to send reset password email to {}", email, e);
        }
    }
}
