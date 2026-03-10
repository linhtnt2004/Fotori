package com.example.fotori.service;

public interface NotificationCommandService {

    void markAsRead(String email, Long notificationId);

    void markAllAsRead(String email);
}