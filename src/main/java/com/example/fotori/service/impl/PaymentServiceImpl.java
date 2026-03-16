package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.dto.CreatePaymentRequest;
import com.example.fotori.dto.CreatePaymentResponse;
import com.example.fotori.dto.PaymentHistoryResponse;
import com.example.fotori.dto.PaymentStatusResponse;
import com.example.fotori.dto.admin.AdminPaymentDTO;
import com.example.fotori.model.Booking;
import com.example.fotori.model.Payment;
import com.example.fotori.model.SubscriptionPlan;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PaymentRepository;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.repository.SubscriptionPlanRepository;
import com.example.fotori.service.BookingEmailService;
import com.example.fotori.service.PaymentService;
import com.example.fotori.service.payment.processor.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final List<PaymentProcessor> processors;
    private final BookingEmailService bookingEmailService;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PhotographerProfileRepository photographerRepository;

    @Override
    @Transactional
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {

        String transactionId = UUID.randomUUID().toString();

        PaymentProcessor processor = processors.stream()
            .filter(p -> p.getMethod() == request.getMethod())
            .findFirst()
            .orElseThrow(() ->
                             new RuntimeException("PAYMENT_METHOD_NOT_SUPPORTED")
            );

        Payment payment;
        Double amount;
        String paymentUrl;

        if (request.getBookingId() != null) {

            Booking booking = bookingRepository
                .findById(request.getBookingId())
                .orElseThrow(() ->
                                 new RuntimeException("BOOKING_NOT_FOUND")
                );

            if (booking.getPaymentStatus() == PaymentStatus.PAID) {
                throw new RuntimeException("BOOKING_ALREADY_PAID");
            }

            boolean hasPendingPayment =
                paymentRepository.existsByBooking_IdAndStatus(
                    booking.getId(),
                    PaymentStatus.PENDING
                );

            if (hasPendingPayment) {
                throw new RuntimeException("PAYMENT_ALREADY_PENDING");
            }

            amount =
                booking.getFinalPrice() != null
                    ? booking.getFinalPrice()
                    : booking.getTotalPrice();

            paymentUrl = processor.createPayment(
                booking,
                amount,
                transactionId
            );

            payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .method(request.getMethod())
                .transactionId(transactionId)
                .qrContent(paymentUrl)
                .status(PaymentStatus.PENDING)
                .build();
        }

        else if (request.getPlanId() != null) {

            SubscriptionPlan plan = subscriptionPlanRepository
                .findById(request.getPlanId())
                .orElseThrow(() ->
                                 new RuntimeException("PLAN_NOT_FOUND")
                );

            amount = plan.getPrice().doubleValue();

            paymentUrl = processor.createPayment(
                null,
                amount,
                transactionId
            );

            payment = Payment.builder()
                .subscriptionPlan(plan)
                .amount(amount)
                .method(request.getMethod())
                .transactionId(transactionId)
                .qrContent(paymentUrl)
                .status(PaymentStatus.PENDING)
                .build();
        }

        else {
            throw new RuntimeException("INVALID_PAYMENT_REQUEST");
        }

        paymentRepository.save(payment);

        return CreatePaymentResponse.builder()
            .id(payment.getId())
            .paymentUrl(paymentUrl)
            .transactionId(transactionId)
            .build();
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new RuntimeException("PAYMENT_NOT_FOUND"));

        return PaymentStatusResponse.builder()
            .status(payment.getStatus())
            .amount(payment.getAmount())
            .build();
    }

    @Override
    public Page<PaymentHistoryResponse> getPaymentHistory(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> payments = paymentRepository.findByBooking_User_Id(userId, pageable);

        return payments.map(payment -> PaymentHistoryResponse.builder()
            .id(payment.getId())
            .amount(payment.getAmount())
            .status(payment.getStatus())
            .method(payment.getMethod())
            .createdAt(payment.getCreatedAt())
            .build());
    }

    @Override
    @Transactional
    public void confirmPayment(Long paymentId) {
        Payment payment = paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new RuntimeException("PAYMENT_NOT_FOUND"));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("PAYMENT_ALREADY_CONFIRMED");
        }

        // ── Cập nhật trạng thái ───────────────────────────────
        payment.setStatus(PaymentStatus.PAID);

        Booking booking = payment.getBooking();
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setCustomerStatus(BookingActorStatus.ACCEPTED);
        booking.refreshStatus();

        paymentRepository.save(payment);
        bookingRepository.save(booking);

        // ── Gửi email xác nhận thanh toán ────────────────────
        try {
            bookingEmailService.sendPaymentConfirmedEmails(booking);
        } catch (Exception e) {
            log.warn("Payment email failed for booking {}: {}",
                booking.getId(), e.getMessage());
        }
        // ─────────────────────────────────────────────────────
    }

    @Override
    public List<AdminPaymentDTO> getAllPayments() {
        return paymentRepository
            .findAll(org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "createdAt"))
            .stream()
            .map(p -> AdminPaymentDTO.builder()
                .id(p.getId())
                .bookingId(p.getBooking().getId())
                .amount(p.getAmount())
                .method(p.getMethod() != null ? p.getMethod().name() : "Thủ công")
                .transactionId(p.getTransactionId())
                .status(p.getStatus() != null ? p.getStatus().name() : "PENDING")
                .createdAt(p.getCreatedAt())
                .startTime(p.getBooking().getStartTime())
                .endTime(p.getBooking().getEndTime())
                .build())
            .collect(Collectors.toList());
    }
}