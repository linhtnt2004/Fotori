package com.example.fotori.service.impl;

import com.example.fotori.model.Notification;
import com.example.fotori.model.User;
import com.example.fotori.repository.NotificationRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.NotificationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void markAsRead(String email, Long notificationId) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("NOTIFICATION_NOT_FOUND"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("NO_PERMISSION");
        }

        notification.setIsRead(true);

        notificationRepository.save(notification);
    }
}