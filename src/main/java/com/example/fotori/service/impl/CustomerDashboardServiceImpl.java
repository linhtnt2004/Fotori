package com.example.fotori.service.impl;

import com.example.fotori.dto.CustomerDashboardStatsResponse;
import com.example.fotori.dto.CustomerDashboardResponse;
import com.example.fotori.dto.BookingResponse;
import com.example.fotori.dto.PhotographerResponse;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.CustomerDashboardService;
import com.example.fotori.service.CustomerBookingQueryService;
import com.example.fotori.service.PublicPhotographerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerDashboardServiceImpl implements CustomerDashboardService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CustomerBookingQueryService customerBookingQueryService;
    private final PublicPhotographerService publicPhotographerService;

    @Override
    public CustomerDashboardStatsResponse getCustomerStats(String email) {

        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Long totalBookings =
            bookingRepository.countByUser(user);

        Double totalSpent =
            bookingRepository.getTotalSpent(user);

        Long upcomingBookings =
            bookingRepository.countUpcomingBookings(user);

        return CustomerDashboardStatsResponse.builder()
            .totalBookings(totalBookings)
            .totalSpent(totalSpent)
            .upcomingBookings(upcomingBookings)
            .build();
    }

    @Override
    public CustomerDashboardResponse getCustomerDashboard(String email) {
        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Get stats
        CustomerDashboardStatsResponse stats = getCustomerStats(email);

        // Get recent bookings (last 5)
        List<BookingResponse> recentBookings = customerBookingQueryService
            .getMyBookings(email, null, 0, 5)
            .getContent();

        // Get recommended photographers (top 6)
        List<PhotographerResponse> recommendedPhotographers = publicPhotographerService
            .getPhotographers(0, 6, null, null, null)
            .getContent()
            .stream()
            .map(dto -> PhotographerResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .image(dto.getAvatar())
                .price(dto.getStartingPrice() != null ? dto.getStartingPrice().toString() : "N/A")
                .build())
            .toList();

        return CustomerDashboardResponse.builder()
            .stats(stats)
            .recentBookings(recentBookings)
            .recommendedPhotographers(recommendedPhotographers)
            .build();
    }
}
