package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.CustomerDashboardStatsResponse;
import com.example.fotori.service.CustomerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/dashboard")
@RequiredArgsConstructor
public class CustomerDashboardController {

    private final CustomerDashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getStats(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        CustomerDashboardStatsResponse stats =
            dashboardService.getCustomerStats(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Customer dashboard stats fetched successfully",
                stats
            )
        );
    }
}