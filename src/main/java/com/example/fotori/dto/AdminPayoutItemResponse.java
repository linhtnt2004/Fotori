package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminPayoutItemResponse {

    Long bookingId;

    String photographerName;

    Double totalAmount;
    Double payoutAmount;
    Double platformFee;

    Integer commissionPercent;

    String planName;

    String bankName;
    String bankAccountNumber;
    String bankAccountName;

    String payoutStatus;

    LocalDateTime completedAt;
}
