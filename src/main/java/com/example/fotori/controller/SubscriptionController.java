package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}