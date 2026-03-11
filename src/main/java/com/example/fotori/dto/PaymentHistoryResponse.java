package com.example.fotori.dto;

import com.example.fotori.common.enums.PaymentMethod;
import com.example.fotori.common.enums.PaymentStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentHistoryResponse {

    private Long id;

    private Double amount;

    private PaymentStatus status;

    private PaymentMethod method;

    private LocalDateTime createdAt;
}