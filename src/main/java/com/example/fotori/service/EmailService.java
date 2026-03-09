package com.example.fotori.service;

public interface EmailService {

    void sendVerificationEmail(String email, String token);

    void sendResetPasswordEmail(String email, String token);
}
