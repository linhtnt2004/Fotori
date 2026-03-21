package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.dto.BookingResponse;
import com.example.fotori.model.Booking;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.ReviewRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.CustomerBookingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerBookingQueryServiceImpl implements CustomerBookingQueryService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Page<BookingResponse> getMyBookings(
        String email,
        BookingStatus status,
        int page,
        int size
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Booking> bookings;

        if (status != null) {
            bookings = bookingRepository.findByUserAndStatus(
                user,
                status,
                pageable
            );
        } else {
            bookings = bookingRepository.findByUser(
                user,
                pageable
            );
        }

        return bookings.map(b ->
                                BookingResponse.builder()
                                    .id(b.getId())
                                    .photographerName(
                                        b.getPhotographer().getUser().getFullName()
                                    )
                                    .photographerEmail(
                                        b.getPhotographer().getUser().getEmail()
                                    )
                                    .photographerAvatar(
                                        b.getPhotographer().getUser().getAvatarUrl()
                                    )
                                    .customerName(
                                        b.getUser().getFullName()
                                    )
                                    .customerEmail(
                                        b.getUser().getEmail()
                                    )
                                    .customerAvatar(
                                        b.getUser().getAvatarUrl()
                                    )
                                    .packageTitle(
                                        b.getPhotoPackage().getTitle()
                                    )
                                    .startTime(b.getStartTime())
                                    .endTime(b.getEndTime())
                                    .status(b.getStatus())
                                    .paymentStatus(b.getPaymentStatus())
                                    .payoutStatus(b.getPayoutStatus())
                                    .location(b.getLocation())
                                    .price(b.getFinalPrice())
                                    .details(b.getNote())
                                    .hasReview(reviewRepository.existsByBooking(b))
                                    .build()
        );
    }
}
