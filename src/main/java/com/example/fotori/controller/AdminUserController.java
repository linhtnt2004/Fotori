package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.ApiUserDetailResponse;
import com.example.fotori.dto.UpdateUserStatusRequest;
import com.example.fotori.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PutMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse> updateUserStatus(
        @PathVariable Long id,
        @RequestBody UpdateUserStatusRequest request
    ) {

        adminUserService.updateUserStatus(id, request.getStatus());

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "User status updated successfully",
                null
            )
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserDetail(
        @PathVariable Long id
    ) {

        ApiUserDetailResponse user =
            adminUserService.getUserDetail(id);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "User detail fetched successfully",
                user
            )
        );
    }
}
