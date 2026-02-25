package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.dto.UpdateBookingStatusRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Booking;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.CustomerBookingActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBookingActionServiceImpl implements CustomerBookingActionService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void updateStatus(
        String customerEmail,
        Long bookingId,
        UpdateBookingStatusRequest request
    ) {

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

        Booking booking =
            getBookingForCustomer(customerEmail, bookingId);

        if (newStatus == BookingActorStatus.DONE &&
            booking.getPhotographerStatus() != BookingActorStatus.DONE) {

            throw new BusinessException("PHOTOGRAPHER_NOT_DONE_YET");
        }

        booking.setCustomerStatus(newStatus);
        booking.refreshStatus();
        bookingRepository.save(booking);
    }

    private Booking getBookingForCustomer(String email, Long bookingId) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository
            .findByIdAndUser(bookingId, user)
            .orElseThrow(() ->
                             new RuntimeException("Booking not found or not yours")
            );

        if (booking.getCustomerStatus() != BookingActorStatus.PENDING &&
            booking.getCustomerStatus() != BookingActorStatus.ACCEPTED &&
            booking.getCustomerStatus() != BookingActorStatus.IN_PROGRESS) {

            throw new BusinessException("BOOKING_ALREADY_FINALIZED");
        }

        return booking;
    }
}
