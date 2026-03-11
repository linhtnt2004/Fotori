package com.example.fotori.service;

import com.example.fotori.dto.CreatePaymentRequest;
import com.example.fotori.dto.CreatePaymentResponse;

public interface PaymentService {

    CreatePaymentResponse createPayment(CreatePaymentRequest request);
}
