package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.CreateAvailabilityRequest;
import com.example.fotori.service.PhotographerAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photographer/availability")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGRAPHER')")
public class PhotographerAvailabilityController {

    private final PhotographerAvailabilityService service;

    @PostMapping
    public ResponseEntity<ApiResponse> create(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody CreateAvailabilityRequest request
    ) {
        service.createAvailability(userDetails.getUsername(), request);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Availability created",
                null
            )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getMyAvailability(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "My availability",
                service.getMyAvailability(userDetails.getUsername())
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id
    ) {
        service.deleteAvailability(userDetails.getUsername(), id);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Availability deleted",
                null
            )
        );
    }
}