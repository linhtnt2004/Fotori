package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.common.enums.VoucherType;
import com.example.fotori.dto.BookingCreateRequest;
import com.example.fotori.dto.BookingResponse;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.*;
import com.example.fotori.repository.*;
import com.example.fotori.service.BookingEmailService;
import com.example.fotori.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final PhotoPackageRepository photoPackageRepository;
    private final PhotographerAvailabilityRepository availabilityRepository;
    private final VoucherRepository voucherRepository;
    private final BookingEmailService bookingEmailService;

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

        Double packagePrice = photoPackage.getPrice() != null
            ? Double.valueOf(photoPackage.getPrice())
            : 0.0;

        Double serviceFee = 40000.0;
        Double discount = 0.0;

        if (request.getVoucherCode() != null && !request.getVoucherCode().isBlank()) {
            Voucher voucher = voucherRepository
                .findByCodeAndActiveTrue(request.getVoucherCode())
                .orElseThrow(() -> new BusinessException("VOUCHER_NOT_FOUND"));

            if (voucher.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new BusinessException("VOUCHER_EXPIRED");
            }

            if (voucher.getUsageLimit() != null &&
                voucher.getUsedCount() >= voucher.getUsageLimit()) {
                throw new BusinessException("VOUCHER_USAGE_LIMIT");
            }

            if (voucher.getMinOrderValue() != null &&
                packagePrice < voucher.getMinOrderValue()) {
                throw new BusinessException("VOUCHER_MIN_ORDER_NOT_MET");
            }

            if (voucher.getType() == VoucherType.PERCENTAGE) {
                discount = packagePrice * voucher.getValue() / 100.0;
                if (voucher.getMaxDiscount() != null &&
                    discount > voucher.getMaxDiscount()) {
                    discount = Double.valueOf(voucher.getMaxDiscount());
                }
            } else if (voucher.getType() == VoucherType.FIXED) {
                discount = Double.valueOf(voucher.getValue());
            }

            voucher.setUsedCount(voucher.getUsedCount() + 1);
        }

        Double finalPriceToPay = packagePrice + serviceFee - discount;
        if (finalPriceToPay < 0) {
            finalPriceToPay = 0.0;
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
            .totalPrice(packagePrice)
            .finalPrice(finalPriceToPay)
            .location(request.getLocation())
            .build();

        booking.refreshStatus();
        booking = bookingRepository.save(booking);

        // ── Gửi email thông báo booking mới ──────────────────────
        final Booking savedBooking = booking;
        try {
            bookingEmailService.sendBookingCreatedEmails(savedBooking);
        } catch (Exception e) {
            log.warn("Email notification failed for booking {}: {}",
                savedBooking.getId(), e.getMessage());
        }
        // ─────────────────────────────────────────────────────────

        return booking.getId();
    }

    @Override
    public List<BookingResponse> getMyBookings(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        return bookingRepository.findByUserAndPaymentStatus(
                user,
                com.example.fotori.common.enums.PaymentStatus.PAID,
                org.springframework.data.domain.Pageable.unpaged()
            )
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