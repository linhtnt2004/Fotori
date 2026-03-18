package com.example.fotori.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MySubscriptionResponse {
    Long id;
    String planName;
    java.time.LocalDateTime startDate;
    java.time.LocalDateTime endDate;
    long daysRemaining;
    boolean active;
}
