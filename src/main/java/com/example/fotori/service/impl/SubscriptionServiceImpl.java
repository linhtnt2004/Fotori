package com.example.fotori.service.impl;

import com.example.fotori.dto.SubscriptionInfo;
import com.example.fotori.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    @Override
    public SubscriptionInfo getUserSubscription(String email) {
        // Mock implementation - return basic subscription info
        // In real implementation, this would query subscription database
        return SubscriptionInfo.builder()
            .planName("Free Plan")
            .startDate(LocalDateTime.now().minusDays(30))
            .endDate(LocalDateTime.now().plusDays(335)) // 1 year from start
            .active(true)
            .build();
    }
}