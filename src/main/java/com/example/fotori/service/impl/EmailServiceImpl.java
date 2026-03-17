package com.example.fotori.service.impl;

import com.example.fotori.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username:fotori.official@gmail.com}")
    private String fromEmail;

    @Value("${spring.mail.password:}")
    private String apiKey;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.backend-url}")
    private String backendUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @Override
    @Async
    public void sendVerificationEmail(String email, String token) {
        log.info("[START] sendVerificationEmail (API MODE) to: {}", email);
        String verifyUrl = backendUrl + "/auth/verify-email?token=" + token;
        
        String content = "Click to verify your account:<br/><a href='" + verifyUrl + "'>" + verifyUrl + "</a>";
        sendHtmlEmail(email, "Verify your email", content);
    }

    @Override
    @Async
    public void sendResetPasswordEmail(String email, String token) {
        log.info("[START] sendResetPasswordEmail (API MODE) to: {}", email);
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        
        String content = "Click the link below to reset your password:<br/><a href='" + resetUrl + "'>" + resetUrl + "</a>";
        sendHtmlEmail(email, "Reset your password", content);
    }

    @Override
    @Async
    public void sendHtmlEmail(String toEmail, String subject, String htmlContent) {
        if (apiKey == null || apiKey.isBlank()) {
            log.error("[API ERROR] Brevo API Key is missing! Check MAIL_PASSWORD in Environment Variables.");
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("sender", Map.of("email", fromEmail, "name", "Fotori"));
            body.put("to", List.of(Map.of("email", toEmail)));
            body.put("subject", subject);
            body.put("htmlContent", "<html><body>" + htmlContent + "</body></html>");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            log.info("Sending request to Brevo API for {}", toEmail);
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_API_URL, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("[SUCCESS] Email sent via API to {}", toEmail);
            } else {
                log.error("[API ERROR] Brevo returned status {}: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("[UNEXPECTED ERROR] Failed to send email via API to {}: {}", toEmail, e.getMessage(), e);
        }
    }
}
