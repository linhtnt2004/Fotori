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
        log.info("[START] sendVerificationEmail to: {}", email);
        try {
            log.info("Step 1: Constructing URL. backendUrl={}", backendUrl);
            String verifyUrl = backendUrl + "/auth/verify-email?token=" + token;
            log.info("Step 2: URL constructed: {}", verifyUrl);

            log.info("Step 3: Creating SimpleMailMessage");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            
            log.info("Step 4: Setting From address. fromAddress={}", fromAddress);
            if (fromAddress != null && !fromAddress.isBlank()) {
                message.setFrom(fromAddress);
                log.info("Step 4a: SetFrom: {}", fromAddress);
            } else {
                log.warn("Step 4b: fromAddress is empty!");
            }
            
            message.setSubject("Verify your email");
            message.setText("Click to verify your account:\n" + verifyUrl);
            log.info("Step 5: Message prepared. Attempting mailSender.send...");

            mailSender.send(message);
            log.info("[SUCCESS] Verification email sent to {}", email);
        } catch (MailException e) {
            log.error("[MAIL ERROR] Failed to send to {}: {}", email, e.getMessage(), e);
        } catch (Exception e) {
            log.error("[UNEXPECTED ERROR] Failed to send to {}: {}", email, e.getMessage(), e);
        } finally {
            log.info("[END] sendVerificationEmail thread finished for {}", email);
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
