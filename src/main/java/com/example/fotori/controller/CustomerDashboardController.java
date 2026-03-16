package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.CustomerDashboardResponse;
import com.example.fotori.service.CustomerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerDashboardController {

    private final CustomerDashboardService dashboardService;

    @GetMapping("/me/dashboard")
    public ResponseEntity<ApiResponse> getMyDashboard(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        CustomerDashboardResponse dashboard =
            dashboardService.getCustomerDashboard(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Customer dashboard fetched successfully",
                dashboard
            )
        );
    }
}