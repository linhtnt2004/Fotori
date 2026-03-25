package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.dto.DashboardRecentBookingResponse;
import com.example.fotori.dto.DashboardRecentReviewResponse;
import com.example.fotori.dto.DashboardTransactionResponse;
import com.example.fotori.dto.PhotographerDashboardStatsResponse;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Review;
import com.example.fotori.model.User;
import com.example.fotori.model.Payment;
import com.example.fotori.repository.*;
import com.example.fotori.service.PhotographerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotographerDashboardServiceImpl implements PhotographerDashboardService {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public PhotographerDashboardStatsResponse getStats(String email) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        long totalBookings =
            bookingRepository.findByPhotographerAndStatus(
                photographer, 
                com.example.fotori.common.enums.BookingStatus.DONE
            ).size();

        long pendingBookings =
            bookingRepository.countByPhotographerAndPhotographerStatus(
                photographer,
                BookingActorStatus.PENDING
            );

        Double totalRevenue =
            bookingRepository.getTotalRevenue(photographer);

        long totalReviews =
            reviewRepository.countByPhotographer(photographer);

        Double averageRating =
            reviewRepository.getAverageRating(photographer);

        return PhotographerDashboardStatsResponse.builder()
            .totalBookings(totalBookings)
            .pendingBookings(pendingBookings)
            .totalRevenue(totalRevenue)
            .totalReviews(totalReviews)
            .averageRating(averageRating)
            .build();
    }

    @Override
    public List<DashboardRecentBookingResponse> getRecentBookings(String email) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        List<Booking> bookings =
            bookingRepository.findByPhotographerOrderByCreatedAtDesc(
                photographer,
                PageRequest.of(0, 5)
            );

        return bookings.stream()
            .map(b -> DashboardRecentBookingResponse.builder()
                .id(b.getId())
                .customerName(b.getUser().getFullName())
                .packageName(b.getPhotoPackage().getTitle())
                .startTime(b.getStartTime())
                .status(b.getStatus())
                .payoutStatus(b.getPayoutStatus())
                .finalPrice(b.getFinalPrice())
                .build())
            .toList();
    }

    @Override
    public List<DashboardRecentReviewResponse> getRecentReviews(String email) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        List<Review> reviews =
            reviewRepository.findByPhotographerOrderByCreatedAtDesc(
                photographer,
                PageRequest.of(0,5)
            );

        return reviews.stream()
            .map(r -> DashboardRecentReviewResponse.builder()
                .id(r.getId())
                .customerName(r.getCustomer().getFullName())
                .customerAvatar(r.getCustomer().getAvatarUrl())
                .rating(r.getRating())
                .comment(r.getComment())
                .build())
            .toList();
    }

    @Override
    public List<DashboardTransactionResponse> getRecentTransactions(String email) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        List<Payment> payments =
            paymentRepository.findByPhotographer_Id(
                photographer.getId(),
                PageRequest.of(0, 5, Sort.by("createdAt").descending())
            ).getContent();

        return payments.stream()
            .map(p -> DashboardTransactionResponse.builder()
                .id(p.getId())
                .type(p.getBooking() != null ? "BOOKING_REVENUE" : "SUBSCRIPTION_PAYMENT")
                .partnerName(p.getBooking() != null ? p.getBooking().getUser().getFullName() : "Fotori")
                .description(p.getBooking() != null ? p.getBooking().getPhotoPackage().getTitle() : (p.getSubscriptionPlan() != null ? p.getSubscriptionPlan().getName() : "Payment"))
                .amount(p.getAmount())
                .createdAt(p.getCreatedAt())
                .status(p.getStatus() != null ? p.getStatus().name() : "PAID")
                .build())
            .toList();
    }
}
