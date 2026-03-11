package com.example.fotori.service.impl;

import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.dto.CreatePaymentRequest;
import com.example.fotori.dto.CreatePaymentResponse;
import com.example.fotori.dto.PaymentStatusResponse;
import com.example.fotori.model.Booking;
import com.example.fotori.model.Payment;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PaymentRepository;
import com.example.fotori.service.PaymentService;
import com.example.fotori.service.payment.processor.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final List<PaymentProcessor> processors;

    @Override
    @Transactional
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {

        Booking booking = bookingRepository
            .findById(request.getBookingId())
            .orElseThrow(() ->
                             new RuntimeException("BOOKING_NOT_FOUND")
            );

        String transactionId = UUID.randomUUID().toString();

        PaymentProcessor processor = processors.stream()
            .filter(p -> p.getMethod() == request.getMethod())
            .findFirst()
            .orElseThrow(() ->
                             new RuntimeException("PAYMENT_METHOD_NOT_SUPPORTED")
            );

        String paymentUrl = processor.createPayment(
            booking,
            request.getAmount(),
            transactionId
        );

        Payment payment = Payment.builder()
            .booking(booking)
            .amount(request.getAmount())
            .method(request.getMethod())
            .transactionId(transactionId)
            .status(PaymentStatus.PENDING)
            .build();

        paymentRepository.save(payment);

        return CreatePaymentResponse.builder()
            .paymentUrl(paymentUrl)
            .transactionId(transactionId)
            .build();
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(Long paymentId) {

        Payment payment = paymentRepository
            .findById(paymentId)
            .orElseThrow(() ->
                             new RuntimeException("PAYMENT_NOT_FOUND")
            );

        return PaymentStatusResponse.builder()
            .status(payment.getStatus())
            .amount(payment.getAmount())
            .build();
    }
}