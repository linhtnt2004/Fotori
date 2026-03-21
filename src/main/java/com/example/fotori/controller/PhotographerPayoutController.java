package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.service.PayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photographer/payouts")
@RequiredArgsConstructor
public class PhotographerPayoutController {

    private final PayoutService payoutService;

    @PostMapping("/{bookingId}/confirm-receipt")
    public ResponseEntity<ApiResponse> confirmReceipt(@PathVariable Long bookingId) {
        payoutService.confirmReceipt(bookingId);
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Payout receipt confirmed successfully",
                null
            )
        );
    }
}
