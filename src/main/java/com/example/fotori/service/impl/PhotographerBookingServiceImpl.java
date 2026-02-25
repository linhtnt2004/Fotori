package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.dto.PhotographerBookingResponse;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PhotographerBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotographerBookingServiceImpl implements PhotographerBookingService {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final BookingRepository bookingRepository;


    @Override
    @Transactional
    public List<PhotographerBookingResponse> getMyBookings(
        String photographerEmail,
        BookingStatus status
    ) {

        User user = userRepository.findByEmail(photographerEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Not a photographer"));

        List<Booking> bookings = (status == null)
            ? bookingRepository.findByPhotographer(photographer)
            : bookingRepository.findByPhotographerAndStatus(photographer, status);

        return bookings.stream()
            .map(b -> new PhotographerBookingResponse(
                b.getId(),
                b.getUser().getId(),
                b.getUser().getEmail(),
                b.getUser().getFullName(),
                b.getPhotoPackage().getId(),
                b.getPhotoPackage().getTitle(),
                b.getStartTime(),
                b.getEndTime(),
                b.getStatus(),
                b.getNote()
            ))
            .toList();
    }
}
