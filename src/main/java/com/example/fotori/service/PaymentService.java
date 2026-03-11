package com.example.fotori.service;

import com.example.fotori.dto.CreatePaymentRequest;
import com.example.fotori.dto.CreatePaymentResponse;
import com.example.fotori.dto.PaymentHistoryResponse;
import com.example.fotori.dto.PaymentStatusResponse;
import org.springframework.data.domain.Page;

public interface PaymentService {

    CreatePaymentResponse createPayment(CreatePaymentRequest request);

    PaymentStatusResponse getPaymentStatus(Long paymentId);

    Page<PaymentHistoryResponse> getPaymentHistory(
        int page,
        int size,
        Long userId
    );

    void confirmPayment(Long paymentId);

    java.util.List<com.example.fotori.dto.admin.AdminPaymentDTO> getAllPayments();
}
