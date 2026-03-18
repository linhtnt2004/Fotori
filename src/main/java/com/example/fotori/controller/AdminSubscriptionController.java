package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/subscriptions")
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> getAllPayments() {
        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription payments fetched",
                subscriptionService.getAllSubscriptionPayments()
            )
        );
    }

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

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<ApiResponse> deleteSubscriptionPayment(
        @PathVariable Long paymentId
    ) {
        subscriptionService.deleteSubscriptionPayment(paymentId);
        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription payment deleted",
                null
            )
        );
    }

    @GetMapping("/photographer/{photographerId}")
    public ResponseEntity<ApiResponse> getPhotographerSubscription(
        @PathVariable Long photographerId
    ) {
        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription fetched",
                subscriptionService.getSubscriptionByPhotographerId(photographerId)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSubscription(
        @PathVariable Long id
    ) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription deleted",
                null
            )
        );
    }
}