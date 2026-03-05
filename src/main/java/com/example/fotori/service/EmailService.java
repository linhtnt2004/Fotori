package com.example.fotori.service;

public interface EmailService {

    void sendVerificationEmail(String email, String token);
}
