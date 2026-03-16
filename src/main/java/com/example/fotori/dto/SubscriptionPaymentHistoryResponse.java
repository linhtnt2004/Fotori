package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionPaymentHistoryResponse {

    String planName;

    Double amount;

    String status;

    String transactionId;

    LocalDateTime createdAt;
}
