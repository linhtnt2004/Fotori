package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.ErrorCode;
import com.example.fotori.dto.CustomerProfile;
import com.example.fotori.dto.UpdateProfileRequest;
import com.example.fotori.model.Customer;
import com.example.fotori.security.UserPrincipal;
import com.example.fotori.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private static final String UPLOAD_DIR = "uploads/avatars";

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyProfile(
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        Customer customer = customerService.findById(principal.getId());

        CustomerProfile profile = CustomerProfile.fromEntity(customer);
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Profile retrieved",
                profile
            )
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateMyProfile(
        @AuthenticationPrincipal UserPrincipal principal,
        @RequestBody UpdateProfileRequest request
    ) {
        Customer update = customerService.updateProfile(principal.getId(), request);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Profile updated",
                update
            )
        );
    }

    @PutMapping(
        value = "/me/avatar",
        consumes = "multipart/form-data"
    )
    public ResponseEntity<ApiResponse> updateAvatar(
        @AuthenticationPrincipal UserPrincipal principal,
        @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ApiResponse("BAD_REQUEST", "File is empty", null)
            );
        }

        String avatarUrl = customerService.updateAvatar(
            principal.getId(),
            file
        );

        return ResponseEntity.ok(
            new ApiResponse("SUCCESS", "Avatar updated", avatarUrl)
        );
    }

}
