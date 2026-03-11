package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.PhotoPackageCreateRequest;
import com.example.fotori.dto.PhotoPackageResponse;
import com.example.fotori.dto.UpdatePhotoPackageRequest;
import com.example.fotori.service.PhotoPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photographer/packages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGRAPHER')")
public class PhotographerPackageController {

    private final PhotoPackageService photoPackageService;

    @GetMapping
    public ResponseEntity<ApiResponse> getMyPackages(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "My packages",
                photoPackageService.getMyPackages(userDetails.getUsername())
            )
        );
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePackage(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id,
        @RequestBody UpdatePhotoPackageRequest request
    ) {

        PhotoPackageResponse response =
            photoPackageService.updatePackage(
                userDetails.getUsername(),
                id,
                request
            );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Package updated",
                response
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePackage(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id
    ) {

        photoPackageService.deletePackage(
            userDetails.getUsername(),
            id
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Package deleted",
                null
            )
        );
    }
}
