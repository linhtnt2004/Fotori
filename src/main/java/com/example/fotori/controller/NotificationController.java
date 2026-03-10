package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.NotificationResponse;
import com.example.fotori.service.NotificationCommandService;
import com.example.fotori.service.NotificationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;

    @GetMapping
    public ResponseEntity<ApiResponse> getNotifications(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "false") boolean unreadOnly
    ) {

        Page<NotificationResponse> notifications =
            notificationQueryService.getNotifications(
                userDetails.getUsername(),
                unreadOnly,
                page,
                size
            );

        long unreadCount =
            notificationQueryService.countUnread(
                userDetails.getUsername()
            );

        Map<String, Object> data = Map.of(
            "data", notifications.getContent(),
            "total", notifications.getTotalElements(),
            "unreadCount", unreadCount
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Notifications fetched successfully",
                data
            )
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse> markAsRead(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id
    ) {

        notificationCommandService.markAsRead(
            userDetails.getUsername(),
            id
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Notification marked as read",
                null
            )
        );
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse> markAllAsRead(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        notificationCommandService.markAllAsRead(
            userDetails.getUsername()
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "All notifications marked as read",
                null
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNotification(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id
    ) {

        notificationCommandService.deleteNotification(
            userDetails.getUsername(),
            id
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Notification deleted",
                null
            )
        );
    }
}