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
                .orElseGet(() -> {
                    PhotographerProfile p = new PhotographerProfile();
                    p.setUser(user);
                    p.setApprovalStatus(com.example.fotori.common.enums.ApprovalStatus.APPROVED);
                    return photographerRepository.save(p);
                });

        Pageable pageable = PageRequest.of(page, size);

        Page<Booking> bookings;

        // Requirement: Only show bookings that have been paid to Admin
        bookings = bookingRepository.findByPhotographerAndPaymentStatus(
            photographer,
            com.example.fotori.common.enums.PaymentStatus.PAID,
            pageable
        );

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
                                    .location(b.getLocation())
                                    .price(b.getFinalPrice())
                                    .details(b.getNote())
                                    .build()
        );
    }
}