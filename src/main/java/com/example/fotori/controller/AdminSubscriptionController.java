package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/subscriptions")
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/payments/{paymentId}/confirm")
    public ResponseEntity<ApiResponse> confirmSubscriptionPayment(
        @PathVariable Long paymentId
    ) {

        subscriptionService.confirmSubscriptionPayment(paymentId);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription payment confirmed",
                null
            )
        );
    }
}