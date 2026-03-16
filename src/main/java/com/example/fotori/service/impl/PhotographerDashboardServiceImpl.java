package com.example.fotori.service.impl;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.dto.DashboardRecentBookingResponse;
import com.example.fotori.dto.DashboardRecentReviewResponse;
import com.example.fotori.dto.PhotographerDashboardResponse;
import com.example.fotori.dto.AISuggestionResponse;
import com.example.fotori.dto.PhotographerDashboardStatsResponse;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Review;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.ReviewRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PhotographerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotographerDashboardServiceImpl implements PhotographerDashboardService {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public PhotographerDashboardStatsResponse getStats(String email) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Photographer profile not found"));

        long totalBookings =
            bookingRepository.countByPhotographer(photographer);

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
                .createdAt(r.getCreatedAt())
                .build())
            .toList();
    }

    @Override
    public PhotographerDashboardResponse getPhotographerDashboard(String email) {
        // Get stats
        PhotographerDashboardStatsResponse stats = getStats(email);

        // Get upcoming bookings
        List<DashboardRecentBookingResponse> upcomingBookings = getRecentBookings(email);

        // Mock AI suggestions (can be implemented later)
        List<AISuggestionResponse> aiSuggestions = List.of(
            AISuggestionResponse.builder()
                .title("Tăng giá gói Premium")
                .description("Giá hiện tại thấp hơn 15% so với thị trường")
                .action("Cập nhật giá")
                .build(),
            AISuggestionResponse.builder()
                .title("Thêm ảnh portfolio")
                .description("Portfolio của bạn cần thêm 3 ảnh nữa để tăng tỷ lệ booking")
                .action("Thêm ảnh")
                .build()
        );

        return PhotographerDashboardResponse.builder()
            .stats(stats)
            .upcomingBookings(upcomingBookings)
            .aiSuggestions(aiSuggestions)
            .build();
    }
}
