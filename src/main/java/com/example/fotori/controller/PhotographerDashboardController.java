package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.DashboardRecentBookingResponse;
import com.example.fotori.dto.DashboardRecentReviewResponse;
import com.example.fotori.dto.PhotographerDashboardStatsResponse;
import com.example.fotori.service.PhotographerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photographers/me/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGRAPHER')")
public class PhotographerDashboardController {

    private final PhotographerDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse> getDashboard(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        PhotographerDashboardStatsResponse stats =
            dashboardService.getStats(userDetails.getUsername());
        
        // Return combined data to match frontend expectation
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("stats", stats);
        data.put("upcomingBookings", dashboardService.getRecentBookings(userDetails.getUsername()));
        data.put("aiSuggestions", java.util.Collections.emptyList()); // Placeholder for AI

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Dashboard data fetched successfully",
                data
            )
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getStats(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        PhotographerDashboardStatsResponse stats =
            dashboardService.getStats(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Dashboard stats fetched successfully",
                stats
            )
        );
    }

    @GetMapping("/recent-bookings")
    public ResponseEntity<ApiResponse> getRecentBookings(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        List<DashboardRecentBookingResponse> bookings =
            dashboardService.getRecentBookings(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Recent bookings fetched successfully",
                bookings
            )
        );
    }

    @GetMapping("/recent-reviews")
    public ResponseEntity<ApiResponse> getRecentReviews(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        List<DashboardRecentReviewResponse> reviews =
            dashboardService.getRecentReviews(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Recent reviews fetched successfully",
                reviews
            )
        );
    }
}