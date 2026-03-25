package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotographerPayoutResponse {

    Long bookingId;

    Double totalAmount;
    Double platformFee;
    Double payoutAmount;

    Integer commissionPercent;

    String planName;
    String payoutStatus;
}