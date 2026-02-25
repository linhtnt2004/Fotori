package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.UpdateBookingStatusRequest;
import com.example.fotori.service.PhotographerBookingActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photographer/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGRAPHER')")
public class PhotographerBookingActionController {

    private final PhotographerBookingActionService bookingActionService;

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id,
        @RequestBody UpdateBookingStatusRequest request
    ) {
        bookingActionService.updateStatus(
            userDetails.getUsername(),
            id,
            request
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Booking status updated",
                null
            )
        );
    }
}

