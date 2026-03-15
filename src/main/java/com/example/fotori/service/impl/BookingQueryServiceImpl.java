package com.example.fotori.service.impl;

import com.example.fotori.dto.BookingDetailResponse;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Booking;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.BookingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingQueryServiceImpl implements BookingQueryService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDetailResponse getBookingDetail(
        String email,
        Long bookingId
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                             new BusinessException("USER_NOT_FOUND")
            );

        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() ->
                             new BusinessException("BOOKING_NOT_FOUND")
            );

        boolean isCustomer =
            booking.getUser().getId().equals(user.getId());

        boolean isPhotographer =
            booking.getPhotographer().getUser().getId()
                .equals(user.getId());

        if (!isCustomer && !isPhotographer) {
            throw new BusinessException("NO_PERMISSION");
        }

        return BookingDetailResponse.builder()
            .id(booking.getId())
            .customerName(
                booking.getUser().getFullName()
            )
            .photographerName(
                booking.getPhotographer().getUser().getFullName()
            )
            .photographerId(
                booking.getPhotographer().getId()
            )
            .packageTitle(
                booking.getPhotoPackage().getTitle()
            )
            .price(
                booking.getPhotoPackage().getPrice()
            )
            .startTime(booking.getStartTime())
            .endTime(booking.getEndTime())
            .status(booking.getStatus())
            .note(booking.getNote())
            .build();
    }
}