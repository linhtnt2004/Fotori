package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.SubscriptionInfo;
import com.example.fotori.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMySubscription(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        SubscriptionInfo subscription =
            subscriptionService.getUserSubscription(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Subscription info fetched successfully",
                subscription
            )
        );
    }
}