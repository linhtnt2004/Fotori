package com.example.fotori.service.payment.processor;

import com.example.fotori.common.enums.PaymentMethod;
import com.example.fotori.model.Booking;

public interface PaymentProcessor {

    PaymentMethod getMethod();

    String createPayment(
        Booking booking,
        Double amount,
        String transactionId,
        String qrContent
    );
}