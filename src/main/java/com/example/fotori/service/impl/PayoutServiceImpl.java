package com.example.fotori.service.impl;

import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.dto.PhotographerPayoutResponse;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.PhotographerSubscription;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerSubscriptionRepository;
import com.example.fotori.service.PayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayoutServiceImpl implements PayoutService {

    private final BookingRepository bookingRepository;
    private final PhotographerSubscriptionRepository subscriptionRepository;

    @Override
    public PhotographerPayoutResponse calculatePayout(Long bookingId) {

        Booking booking = bookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new RuntimeException("BOOKING_NOT_FOUND"));

        if (booking.getPaymentStatus() != PaymentStatus.PAID) {
            throw new RuntimeException("BOOKING_NOT_PAID");
        }

        Double totalAmount = booking.getFinalPrice();

        PhotographerProfile photographer = booking.getPhotographer();

        PhotographerSubscription sub =
            subscriptionRepository
                .findFirstByPhotographerAndActiveTrueOrderByEndDateDesc(photographer)
                .orElse(null);

        int commission;

        if (sub == null) {
            commission = 10;
        } else {
            commission = sub.getPlan().getCommissionPercent();
        }

        double platformFee = totalAmount * commission / 100.0;
        double payoutAmount = totalAmount - platformFee;

        return PhotographerPayoutResponse.builder()
            .bookingId(bookingId)
            .totalAmount(totalAmount)
            .platformFee(platformFee)
            .payoutAmount(payoutAmount)
            .commissionPercent(commission)
            .planName(sub != null ? sub.getPlan().getName() : "DEFAULT")
            .build();
    }
}
