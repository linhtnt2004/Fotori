package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionInfo {

    private String planName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean active;

}