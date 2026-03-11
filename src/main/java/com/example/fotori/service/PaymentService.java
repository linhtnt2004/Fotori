package com.example.fotori.service;

import com.example.fotori.dto.CreatePaymentRequest;
import com.example.fotori.dto.CreatePaymentResponse;
import com.example.fotori.dto.PaymentStatusResponse;

public interface PaymentService {

    CreatePaymentResponse createPayment(CreatePaymentRequest request);

    PaymentStatusResponse getPaymentStatus(Long paymentId);
}
