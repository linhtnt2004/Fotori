package com.example.fotori.dto;

import com.example.fotori.common.enums.PaymentStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentStatusResponse {

    PaymentStatus status;

    Double amount;
}