package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.PhotoPackageCreateRequest;
import com.example.fotori.service.PhotoPackageService;
import com.example.fotori.service.impl.PhotoPackageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photographer/packages")
@RequiredArgsConstructor
public class PhotographerPackageController {

    private final PhotoPackageService photoPackageService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPackage(
        @AuthenticationPrincipal UserDetails userDetails, @RequestBody
        PhotoPackageCreateRequest request
    ) {
        photoPackageService.createPackage(
            userDetails.getUsername(),
            request
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Package created successfully!",
                null
            )
        );
    }
}
