package com.example.fotori.service;

import com.example.fotori.dto.NotificationResponse;
import org.springframework.data.domain.Page;

public interface NotificationQueryService {

    Page<NotificationResponse> getNotifications(
        String email,
        boolean unreadOnly,
        int page,
        int size
    );

    long countUnread(String email);
}