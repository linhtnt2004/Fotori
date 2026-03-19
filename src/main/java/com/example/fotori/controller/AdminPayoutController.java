package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.PhotographerPayoutResponse;
import com.example.fotori.service.PayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                "SUCCESS",
                "Payout calculated",
                data
            )
        );
    }
}