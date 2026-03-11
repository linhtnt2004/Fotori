package com.example.fotori.service.payment.processor;

import com.example.fotori.common.enums.PaymentMethod;
import com.example.fotori.model.Booking;
import com.example.fotori.service.VietQrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankPaymentProcessor implements PaymentProcessor {

    private final VietQrService vietQrService;

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.BANK;
    }

    @Override
    public String createPayment(
        Booking booking,
        Double amount,
        String transactionId
    ) {

        String qrContent = "FOTORI_BOOKING_" + booking.getId();

        return vietQrService.generateQr(amount, qrContent);
    }
}