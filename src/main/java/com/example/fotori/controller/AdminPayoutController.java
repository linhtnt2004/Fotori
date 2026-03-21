package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.AdminPayoutItemResponse;
import com.example.fotori.dto.PhotographerPayoutResponse;
import com.example.fotori.service.PayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payouts")
@RequiredArgsConstructor
public class AdminPayoutController {

    private final PayoutService payoutService;

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse> getPayout(
        @PathVariable Long bookingId
    ) {

        PhotographerPayoutResponse data =
            payoutService.calculatePayout(bookingId);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Payout calculated",
                data
            )
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse> getPendingPayouts() {

        List<AdminPayoutItemResponse> data =
            payoutService.getPendingPayouts();

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Pending payouts",
                data
            )
        );
    }

    @PostMapping("/{bookingId}/mark-transferred")
    public ResponseEntity<ApiResponse> markAsTransferred(@PathVariable Long bookingId) {
        payoutService.markAsTransferred(bookingId);
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Payout marked as transferred successfully",
                null
            )
        );
    }
}