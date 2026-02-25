package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.UpdateCustomerProfileRequest;
import com.example.fotori.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyProfile(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Customer profile",
                customerProfileService.getMyProfile(userDetails.getUsername())
            )
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateMyProfile(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody UpdateCustomerProfileRequest request
    ) {
        customerProfileService.updateMyProfile(
            userDetails.getUsername(),
            request
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Profile updated successfully",
                null
            )
        );
    }
}
