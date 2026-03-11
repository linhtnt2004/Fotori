package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin Dashboard APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Get Dashboard Statistics")
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getStats() {
        return ResponseEntity.ok(new ApiResponse(ErrorCode.SUCCESS.name(), "Success", adminService.getDashboardStats()));
    }

    @Operation(summary = "Get All Users")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse(ErrorCode.SUCCESS.name(), "Success", adminService.getAllUsers()));
    }

    @Operation(summary = "Get All Bookings")
    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllBookings() {
        return ResponseEntity.ok(new ApiResponse(ErrorCode.SUCCESS.name(), "Success", adminService.getAllBookings()));
    }

    @Operation(summary = "Get All Photographers")
    @GetMapping("/photographers/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllPhotographers() {
        return ResponseEntity.ok(new ApiResponse(ErrorCode.SUCCESS.name(), "Success", adminService.getAllPhotographers()));
    }

}
