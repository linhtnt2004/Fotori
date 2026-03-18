package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.MySubscriptionResponse;
import com.example.fotori.dto.SubscriptionHistoryResponse;
import com.example.fotori.dto.SubscriptionPaymentHistoryResponse;
import com.example.fotori.service.SubscriptionService;
import com.example.fotori.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/plans")
    public ResponseEntity<ApiResponse> getPlans() {

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription plans",
                subscriptionService.getPlans()
            )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMySubscription(
        Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        MySubscriptionResponse response =
            subscriptionService.getMySubscription(userDetails.getUser().getId());

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription fetched",
                response
            )
        );
    }

    @GetMapping("/payment-history")
    public ResponseEntity<ApiResponse> getSubscriptionPaymentHistory(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Page<SubscriptionPaymentHistoryResponse> response =
            subscriptionService.getSubscriptionPayments(
                userDetails.getUser().getId(),
                page,
                size
            );

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription payment history fetched",
                response
            )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getSubscriptionHistory(
        Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<SubscriptionHistoryResponse> data =
            subscriptionService.getSubscriptionHistory(userDetails.getUser().getId());

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription history fetched",
                data
            )
        );
    }
}