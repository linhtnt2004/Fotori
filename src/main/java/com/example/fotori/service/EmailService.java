package com.example.fotori.service;

public interface EmailService {

    void sendHtmlEmail(String to, String subject, String body);

    void sendVerificationEmail(String email, String token);

    void sendResetPasswordEmail(String email, String token);
}
