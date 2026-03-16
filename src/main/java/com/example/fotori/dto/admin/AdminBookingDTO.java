package com.example.fotori.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminBookingDTO {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String photographerName;
    private String packageTitle;
    private Double packagePrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String paymentStatus;
    private LocalDateTime createdAt;
}
