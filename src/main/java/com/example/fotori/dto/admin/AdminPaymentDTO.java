package com.example.fotori.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentDTO {
    private Long id;
    private Long bookingId;
    private Double amount;
    private String method;
    private String transactionId;
    private String status;
    private LocalDateTime createdAt;
}
