package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.dto.UpdateBookingStatusRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PhotographerBookingActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PhotographerBookingActionServiceImpl implements PhotographerBookingActionService {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final BookingRepository bookingRepository;


    @Override
    @Transactional
    public void updateStatus(
        String photographerEmail,
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

        if (newStatus == BookingActorStatus.CANCELLED) {
            throw new BusinessException("PHOTOGRAPHER_CANNOT_CANCEL");
        }

        Booking booking =
            getPendingBookingForPhotographer(photographerEmail, bookingId);

        if (newStatus == BookingActorStatus.ACCEPTED) {
            boolean conflict =
                bookingRepository.existsAcceptedOverlapping(
                    booking.getPhotographer(),
                    booking.getStartTime(),
                    booking.getEndTime()
                );

            if (conflict) {
                throw new BusinessException("BOOKING_TIME_CONFLICT");
            }
        }

        booking.setPhotographerStatus(newStatus);
        booking.refreshStatus();
    }


    private Booking getPendingBookingForPhotographer(String email, Long bookingId) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Not a photographer"));

        Booking booking = bookingRepository
            .findByIdAndPhotographer(bookingId, photographer)
            .orElseThrow(() ->
                             new RuntimeException("Booking not found or not yours")
            );

        if (booking.getPhotographerStatus() != BookingActorStatus.PENDING) {
            throw new BusinessException("BOOKING_ALREADY_HANDLED");
        }

        return booking;
    }
}
