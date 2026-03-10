package com.example.fotori.service.impl;

import com.example.fotori.dto.CustomerDashboardStatsResponse;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.CustomerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDashboardServiceImpl implements CustomerDashboardService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

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
}
