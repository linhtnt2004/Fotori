package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.dto.BookingResponse;
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
    public Long createBooking(String userEmail, BookingCreateRequest request) {

        validateTime(request.getStartTime(), request.getEndTime());

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile photographer = photographerRepository
                .findById(request.getPhotographerId())
                .orElseThrow(() -> new BusinessException("PHOTOGRAPHER_NOT_FOUND"));

        if (photographer.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new BusinessException("PHOTOGRAPHER_NOT_APPROVED");
        }

        boolean isWithinAvailability = availabilityRepository
                .existsByPhotographerAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndDeletedAtIsNull(
                        photographer,
                        request.getStartTime(),
                        request.getEndTime());

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
                        request.getEndTime())
                .isEmpty();

        if (hasConflict) {
            throw new BusinessException("PHOTOGRAPHER_BUSY");
        }

        Double packagePrice = photoPackage.getPrice() != null ? Double.valueOf(photoPackage.getPrice()) : 0.0;
        Double finalPriceToPay = packagePrice + 40000.0;

        Booking booking = Booking.builder()
                .user(user)
                .photographer(photographer)
                .photoPackage(photoPackage)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .note(request.getNote())
                .customerStatus(BookingActorStatus.PENDING)
                .photographerStatus(BookingActorStatus.PENDING)
                .totalPrice(packagePrice)
                .finalPrice(finalPriceToPay)
                .location(request.getLocation())
                .build();

        booking.refreshStatus();
        booking = bookingRepository.save(booking);

        return booking.getId();
    }

    @Override
    public List<BookingResponse> getMyBookings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        // Only show PAID bookings to the customer
        return bookingRepository.findByUserAndPaymentStatus(
                user,
                com.example.fotori.common.enums.PaymentStatus.PAID,
                org.springframework.data.domain.Pageable.unpaged())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> getPhotographerBookings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile photographer = photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Not a photographer"));

        return bookingRepository.findByPhotographer(photographer)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .photographerName(booking.getPhotographer().getUser().getFullName())
                .customerName(booking.getUser().getFullName())
                .packageTitle(booking.getPhotoPackage().getTitle())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .paymentStatus(booking.getPaymentStatus())
                .location(booking.getLocation())
                .price(booking.getFinalPrice())
                .details(booking.getNote())
                .build();
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
