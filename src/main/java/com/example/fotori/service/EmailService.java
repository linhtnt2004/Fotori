package com.example.fotori.service;

public interface EmailService {

    void sendHtmlEmail(String to, String subject, String body);

    void sendVerificationEmail(String email, String token);

    void sendResetPasswordEmail(String email, String token);
    
    void sendAdminNewPhotographerNotification(String photographerName, String photographerEmail);
    
    void sendPhotographerApprovalNotification(String toEmail, String photographerName);

    void sendPhotographerPendingNotification(String toEmail, String photographerName);
}
