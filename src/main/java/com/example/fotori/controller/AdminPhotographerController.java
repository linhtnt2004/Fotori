package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.UpdateApprovalStatusRequest;
import com.example.fotori.service.AdminPhotographerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/photographers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPhotographerController {

    private final AdminPhotographerService adminPhotographerService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse> getPendingPhotographers() {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Pending photographers",
                adminPhotographerService.getPendingPhotographers()
            )
        );
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(
        @PathVariable Long id,
        @RequestBody UpdateApprovalStatusRequest request
    ) {
        adminPhotographerService.updatePhotographerStatus(id, request);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer status updated",
                null
            )
        );
    }
}
