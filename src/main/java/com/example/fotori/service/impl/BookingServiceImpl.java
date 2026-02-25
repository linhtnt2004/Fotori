package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.dto.BookingCalendarResponse;
import com.example.fotori.dto.BookingCreateRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotoPackage;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.repository.*;
import com.example.fotori.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final PhotoPackageRepository photoPackageRepository;
    private final PhotographerAvailabilityRepository availabilityRepository;

    @Override
    @Transactional
    public void createBooking(String userEmail, BookingCreateRequest request) {

        validateTime(request.getStartTime(), request.getEndTime());

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile photographer = photographerRepository
            .findById(request.getPhotographerId())
            .orElseThrow(() -> new BusinessException("PHOTOGRAPHER_NOT_FOUND"));

        if (photographer.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new BusinessException("PHOTOGRAPHER_NOT_APPROVED");
        }

        boolean isWithinAvailability =
            availabilityRepository
                .existsByPhotographerAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndDeletedAtIsNull(
                    photographer,
                    request.getStartTime(),
                    request.getEndTime()
                );

        if (!isWithinAvailability) {
            throw new BusinessException("BOOKING_OUTSIDE_AVAILABILITY");
        }

        PhotoPackage photoPackage = photoPackageRepository
            .findById(request.getPhotoPackageId())
            .orElseThrow(() -> new BusinessException("PHOTO_PACKAGE_NOT_FOUND"));

        if (!photoPackage.getPhotographerProfile().getId()
            .equals(photographer.getId())) {
            throw new BusinessException("PACKAGE_NOT_BELONG_TO_PHOTOGRAPHER");
        }

        boolean hasConflict = !bookingRepository
            .findAcceptedOverlappingBookings(
                photographer,
                request.getStartTime(),
                request.getEndTime()
            )
            .isEmpty();

        if (hasConflict) {
            throw new BusinessException("PHOTOGRAPHER_BUSY");
        }

        Booking booking = Booking.builder()
            .user(user)
            .photographer(photographer)
            .photoPackage(photoPackage)
            .startTime(request.getStartTime())
            .endTime(request.getEndTime())
            .note(request.getNote())
            .customerStatus(BookingActorStatus.PENDING)
            .photographerStatus(BookingActorStatus.PENDING)
            .build();

        booking.refreshStatus();
        bookingRepository.save(booking);
    }


    @Override
    public List<BookingCalendarResponse> getMyBookings(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        return bookingRepository.findByUser(user)
            .stream()
            .map(this::toCalendarResponse)
            .toList();
    }

    @Override
    public List<BookingCalendarResponse> getPhotographerBookings(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Not a photographer"));

        return bookingRepository.findByPhotographer(photographer)
            .stream()
            .map(this::toCalendarResponse)
            .toList();
    }

    private BookingCalendarResponse toCalendarResponse(Booking booking) {
        return new BookingCalendarResponse(
            booking.getId(),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getPhotoPackage().getTitle(),
            booking.getStatus()
        );
    }

    private void validateTime(LocalDateTime start, LocalDateTime end) {

        if (start == null || end == null) {
            throw new BusinessException("TIME_REQUIRED");
        }

        if (!end.isAfter(start)) {
            throw new BusinessException("END_TIME_MUST_AFTER_START");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new BusinessException("START_TIME_IN_PAST");
        }
    }

}
