package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPayments() {
        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Payments fetched",
                paymentService.getAllPayments()
            )
        );
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse> confirmPayment(
        @PathVariable Long id
    ) {

        paymentService.confirmPayment(id);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Payment confirmed",
                null
            )
        );
    }

    @GetMapping("/subscriptions/payments")
    public ResponseEntity<ApiResponse> getSubscriptionPayments() {
        // For now, return same as regular payments
        // In real implementation, filter by subscription payments only
        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription payments fetched",
                paymentService.getAllPayments()
            )
        );
    }

    @PostMapping("/subscriptions/payments/{id}/confirm")
    public ResponseEntity<ApiResponse> confirmSubscriptionPayment(
        @PathVariable Long id
    ) {

        paymentService.confirmPayment(id);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription payment confirmed",
                null
            )
        );
    }
}