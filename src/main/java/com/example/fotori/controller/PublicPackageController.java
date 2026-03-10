package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.service.PhotoPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/packages")
@RequiredArgsConstructor
public class PublicPackageController {

    private final PhotoPackageService photoPackageService;

    @GetMapping
    public ResponseEntity<ApiResponse> getPackages() {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Active photo packages",
                photoPackageService.getAllActivePackage()
            )
        );
    }
}
