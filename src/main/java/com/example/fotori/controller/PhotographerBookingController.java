package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.service.PhotographerBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photographer/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGRAPHER')")
public class PhotographerBookingController {

    private final PhotographerBookingService photographerBookingService;

    @GetMapping
    public ResponseEntity<ApiResponse> getMyBookings(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(required = false) BookingStatus status
    ) {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer bookings",
                photographerBookingService.getMyBookings(
                    userDetails.getUsername(),
                    status
                )
            )
        );
    }
}
