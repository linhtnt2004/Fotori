package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.dto.BookingResponse;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PhotographerBookingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotographerBookingQueryServiceImpl
    implements PhotographerBookingQueryService {

    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Page<BookingResponse> getPhotographerBookings(
        String email,
        BookingStatus status,
        int page,
        int size
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                             new BusinessException("USER_NOT_FOUND")
            );

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() ->
                                 new BusinessException("PROFILE_NOT_FOUND")
                );

        Pageable pageable = PageRequest.of(page, size);

        Page<Booking> bookings;

        if (status != null) {
            bookings = bookingRepository.findByPhotographerAndStatus(
                photographer,
                status,
                pageable
            );
        } else {
            bookings = bookingRepository.findByPhotographer(
                photographer,
                pageable
            );
        }

        return bookings.map(b ->
                                BookingResponse.builder()
                                    .id(b.getId())
                                    .photographerName(
                                        b.getPhotographer().getUser().getFullName()
                                    )
                                    .packageTitle(
                                        b.getPhotoPackage().getTitle()
                                    )
                                    .startTime(b.getStartTime())
                                    .endTime(b.getEndTime())
                                    .status(b.getStatus())
                                    .build()
        );
    }
}