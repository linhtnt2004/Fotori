package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DashboardTransactionResponse {
    Long id;
    String type; // "BOOKING_REVENUE", "SUBSCRIPTION_PAYMENT"
    String partnerName; // Customer Name or "Fotori"
    String description; // Package Name or Subscription Plan Name
    Double amount;
    LocalDateTime createdAt;
    String status; // "PAID", "PENDING", etc.
}
