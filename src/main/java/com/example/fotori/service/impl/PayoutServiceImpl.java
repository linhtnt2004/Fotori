package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.common.enums.PayoutStatus;
import com.example.fotori.dto.AdminPayoutItemResponse;
import com.example.fotori.dto.PhotographerPayoutResponse;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.PhotographerSubscription;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerSubscriptionRepository;
import com.example.fotori.service.PayoutEmailService;
import com.example.fotori.service.PayoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayoutServiceImpl implements PayoutService {

    private final BookingRepository bookingRepository;
    private final PhotographerSubscriptionRepository subscriptionRepository;
    private final PayoutEmailService payoutEmailService;

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

        double platformFee = (totalAmount * commission / 100.0) + 40000;
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

    @Override
    public List<AdminPayoutItemResponse> getPendingPayouts() {

        List<Booking> bookings =
            bookingRepository.findByPaymentStatusAndStatusAndPayoutStatus(
                PaymentStatus.PAID,
                BookingStatus.DONE,
                PayoutStatus.PENDING
            );

        return bookings.stream().map(booking -> {

            PhotographerProfile photographer = booking.getPhotographer();

            PhotographerSubscription sub =
                subscriptionRepository
                    .findFirstByPhotographerAndActiveTrueOrderByEndDateDesc(photographer)
                    .orElse(null);

            int commission = (sub != null)
                ? sub.getPlan().getCommissionPercent()
                : 10;

            double total = booking.getFinalPrice();
            double fee = (total * commission / 100.0) + 40000;
            double payout = total - fee;

            return AdminPayoutItemResponse.builder()
                .bookingId(booking.getId())
                .photographerName(photographer.getUser().getFullName())
                .totalAmount(total)
                .platformFee(fee)
                .payoutAmount(payout)
                .commissionPercent(commission)
                .planName(sub != null ? sub.getPlan().getName() : "DEFAULT")
                .bankName(photographer.getBankName())
                .bankAccountNumber(photographer.getBankAccountNumber())
                .bankAccountName(photographer.getBankAccountName())
                .payoutStatus(booking.getPayoutStatus().name())
                .completedAt(booking.getUpdatedAt())
                .build();

        }).toList();
    }

    @Override
    @Transactional
    public void markAsTransferred(Long bookingId) {
        Booking booking = bookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new RuntimeException("BOOKING_NOT_FOUND"));

        if (booking.getPaymentStatus() != PaymentStatus.PAID || 
            booking.getStatus() != BookingStatus.DONE) {
            throw new RuntimeException("BOOKING_NOT_READY_FOR_PAYOUT");
        }

        if (booking.getPayoutStatus() != PayoutStatus.PENDING) {
            throw new RuntimeException("PAYOUT_STATUS_NOT_PENDING");
        }

        booking.setPayoutStatus(PayoutStatus.TRANSFERRED);
        bookingRepository.save(booking);
        // Optionally notify photographer that money is on the way
    }

    @Override
    @Transactional
    public void confirmReceipt(Long bookingId) {
        Booking booking = bookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new RuntimeException("BOOKING_NOT_FOUND"));

        if (booking.getPayoutStatus() != PayoutStatus.TRANSFERRED) {
            throw new RuntimeException("PAYOUT_NOT_TRANSFERRED_YET");
        }

        booking.setPayoutStatus(PayoutStatus.PAID);
        bookingRepository.save(booking);

        try {
            payoutEmailService.sendPayoutCompletedEmail(booking);
        } catch (Exception e) {
            log.warn("Payout email failed for booking {}: {}",
                     booking.getId(), e.getMessage());
        }
    }
}
