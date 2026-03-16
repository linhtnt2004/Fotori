package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MySubscriptionResponse {

    String planName;

    LocalDateTime startDate;

    LocalDateTime endDate;

    long daysRemaining;

    boolean active;
}
