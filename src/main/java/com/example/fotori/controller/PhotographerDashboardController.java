package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.DashboardRecentBookingResponse;
import com.example.fotori.dto.DashboardRecentReviewResponse;
import com.example.fotori.dto.PhotographerDashboardResponse;
import com.example.fotori.service.PhotographerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photographers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGRAPHER')")
public class PhotographerDashboardController {

    private final PhotographerDashboardService dashboardService;

    @GetMapping("/me/dashboard")
    public ResponseEntity<ApiResponse> getMyDashboard(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        PhotographerDashboardResponse dashboard =
            dashboardService.getPhotographerDashboard(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer dashboard fetched successfully",
                dashboard
            )
        );
    }
}