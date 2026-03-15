package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.dto.UpdateBookingStatusRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Booking;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.BookingEmailService;
import com.example.fotori.service.CustomerBookingActionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerBookingActionServiceImpl implements CustomerBookingActionService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingEmailService bookingEmailService; // ← THÊM MỚI

    @Override
    @Transactional
    public void updateStatus(
            String customerEmail,
            Long bookingId,
            UpdateBookingStatusRequest request) {

        if (request.getStatus() == null) {
            throw new BusinessException("STATUS_REQUIRED");
        }

        BookingActorStatus newStatus = request.getStatus();

        if (newStatus == BookingActorStatus.PENDING) {
            throw new BusinessException("INVALID_STATUS");
        }

        if (newStatus == BookingActorStatus.IN_PROGRESS) {
            throw new BusinessException("CUSTOMER_CANNOT_SET_IN_PROGRESS");
        }

        Booking booking = getBookingForCustomer(customerEmail, bookingId);

        // ── Lưu trạng thái cũ ────────────────────────────────
        BookingActorStatus oldStatus = booking.getCustomerStatus();

        // ── Cập nhật trạng thái ───────────────────────────────
        booking.setCustomerStatus(newStatus);
        booking.refreshStatus();
        bookingRepository.save(booking);

        // ── Gửi email theo action ─────────────────────────────
        sendEmailByStatus(booking, oldStatus, newStatus);
    }

    // ── Email trigger theo action của Customer ────────────────────────────────
    private void sendEmailByStatus(
            Booking booking,
            BookingActorStatus oldStatus,
            BookingActorStatus newStatus) {
        try {
            switch (newStatus) {
                case CANCELLED -> {
                    // Customer hủy đơn → báo cho Photographer
                    bookingEmailService.sendCustomerCancelledEmail(booking);
                    log.info("Sent CANCELLED email for booking {}", booking.getId());
                }
                case DONE -> {
                    // Customer xác nhận đã nhận ảnh → booking hoàn tất
                    if (booking.getStatus() == BookingStatus.DONE) {
                        bookingEmailService.sendOrderCompletedEmails(booking);
                        log.info("Sent COMPLETED email for booking {}", booking.getId());
                    }
                }
                default -> log.info("No email trigger for customer status: {}", newStatus);
            }
        } catch (Exception e) {
            log.warn("Email notification failed for booking {} status {}: {}",
                booking.getId(), newStatus, e.getMessage());
        }
    }

    private Booking getBookingForCustomer(String email, Long bookingId) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository
            .findByIdAndUser(bookingId, user)
            .orElseThrow(() -> new RuntimeException("Booking not found or not yours"));

        if (booking.getCustomerStatus() != BookingActorStatus.PENDING &&
            booking.getCustomerStatus() != BookingActorStatus.ACCEPTED &&
            booking.getCustomerStatus() != BookingActorStatus.IN_PROGRESS) {
            throw new BusinessException("BOOKING_ALREADY_FINALIZED");
        }

        return booking;
    }
}