package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.BookingResponse;
import com.example.fotori.service.PhotographerBookingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGRAPHER')")
public class PhotographerBookingQueryController {

    private final PhotographerBookingQueryService service;

    @GetMapping("/photographer")
    public ResponseEntity<ApiResponse> getPhotographerBookings(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(required = false) BookingStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {

        Page<BookingResponse> result =
            service.getPhotographerBookings(
                userDetails.getUsername(),
                status,
                page,
                size
            );

        Map<String, Object> data = Map.of(
            "data", result.getContent(),
            "total", result.getTotalElements()
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer bookings",
                data
            )
        );
    }
}
