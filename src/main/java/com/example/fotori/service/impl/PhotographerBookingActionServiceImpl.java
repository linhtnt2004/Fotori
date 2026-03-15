package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.dto.UpdateBookingStatusRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.BookingEmailService;
import com.example.fotori.service.PhotographerBookingActionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotographerBookingActionServiceImpl implements PhotographerBookingActionService {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final BookingRepository bookingRepository;
    private final BookingEmailService bookingEmailService; // ← THÊM MỚI

    @Override
    @Transactional
    public void updateStatus(
            String photographerEmail,
            Long bookingId,
            UpdateBookingStatusRequest request) {

        if (request.getStatus() == null) {
            throw new BusinessException("STATUS_REQUIRED");
        }

        BookingActorStatus newStatus = request.getStatus();

        if (newStatus == BookingActorStatus.PENDING) {
            throw new BusinessException("INVALID_STATUS");
        }

        if (newStatus == BookingActorStatus.CANCELLED) {
            throw new BusinessException("PHOTOGRAPHER_CANNOT_CANCEL");
        }

        Booking booking = getAccessibleBookingForPhotographer(photographerEmail, bookingId);

        // ── Already accepted → skip ───────────────────────────
        if (newStatus == BookingActorStatus.ACCEPTED
                && booking.getPhotographerStatus() == BookingActorStatus.ACCEPTED) {
            return;
        }

        // ── Check conflict khi ACCEPT ─────────────────────────
        if (newStatus == BookingActorStatus.ACCEPTED) {
            boolean conflict = bookingRepository.existsAcceptedOverlapping(
                booking.getPhotographer(),
                booking.getStartTime(),
                booking.getEndTime());
            if (conflict) {
                throw new BusinessException("BOOKING_TIME_CONFLICT");
            }
        }

        // ── Lưu trạng thái cũ để so sánh ─────────────────────
        BookingActorStatus oldStatus = booking.getPhotographerStatus();

        // ── Cập nhật trạng thái ───────────────────────────────
        booking.setPhotographerStatus(newStatus);
        booking.refreshStatus();
        bookingRepository.save(booking);

        // ── Gửi email theo trạng thái mới ────────────────────
        sendEmailByStatus(booking, oldStatus, newStatus);
    }

    // ── Email trigger theo action của Photographer ────────────────────────────
    private void sendEmailByStatus(
            Booking booking,
            BookingActorStatus oldStatus,
            BookingActorStatus newStatus) {
        try {
            switch (newStatus) {
                case ACCEPTED -> {
                    // Photographer xác nhận lịch → báo cho Customer
                    bookingEmailService.sendPhotographerAcceptedEmail(booking);
                    log.info("Sent ACCEPTED email for booking {}", booking.getId());
                }
                case IN_PROGRESS -> {
                    // Đang chụp → không cần email
                    log.info("Booking {} is now IN_PROGRESS", booking.getId());
                }
                case DONE -> {
                    // Giao ảnh xong → báo cho Customer
                    bookingEmailService.sendPhotosDeliveredEmail(booking);
                    log.info("Sent DONE/delivered email for booking {}", booking.getId());
                }
                default -> log.info("No email trigger for status: {}", newStatus);
            }
        } catch (Exception e) {
            log.warn("Email notification failed for booking {} status {}: {}",
                booking.getId(), newStatus, e.getMessage());
        }
    }

    private Booking getAccessibleBookingForPhotographer(String email, Long bookingId) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer = photographerRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Not a photographer"));

        return bookingRepository
            .findByIdAndPhotographer(bookingId, photographer)
            .orElseThrow(() -> new RuntimeException("Booking not found or not yours"));
    }
}