package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.BookingCreateRequest;
import com.example.fotori.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse> createBooking(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody BookingCreateRequest request
    ) {
        bookingService.createBooking(userDetails.getUsername(), request);
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Booking created successfully",
                null
            )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyBookings(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "My bookings",
                bookingService.getMyBookings(userDetails.getUsername())
            )
        );
    }
}
