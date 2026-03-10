package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.BookingDetailResponse;
import com.example.fotori.service.BookingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER','PHOTOGRAPHER')")
public class BookingQueryController {

    private final BookingQueryService service;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBookingDetail(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id
    ) {

        BookingDetailResponse result =
            service.getBookingDetail(
                userDetails.getUsername(),
                id
            );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Booking detail",
                result
            )
        );
    }
}
