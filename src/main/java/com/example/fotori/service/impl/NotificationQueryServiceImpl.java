package com.example.fotori.service.impl;

import com.example.fotori.dto.NotificationResponse;
import com.example.fotori.model.Notification;
import com.example.fotori.model.User;
import com.example.fotori.repository.NotificationRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.NotificationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public Page<NotificationResponse> getNotifications(
        String email,
        boolean unreadOnly,
        int page,
        int size
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        PageRequest pageable = PageRequest.of(page, size);

        Page<Notification> notifications;

        if (unreadOnly) {
            notifications =
                notificationRepository.findByUserAndIsReadFalse(user, pageable);
        } else {
            notifications =
                notificationRepository.findByUser(user, pageable);
        }

        return notifications.map(n ->
                                     NotificationResponse.builder()
                                         .id(n.getId())
                                         .title(n.getTitle())
                                         .content(n.getContent())
                                         .isRead(n.getIsRead())
                                         .createdAt(n.getCreatedAt())
                                         .build()
        );
    }

    @Override
    public long countUnread(String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        return notificationRepository.countByUserAndIsReadFalse(user);
    }
}