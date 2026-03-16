package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionHistoryResponse {

    String planName;

    LocalDateTime startDate;

    LocalDateTime endDate;

    Boolean active;
}
