package com.example.fotori.service;

import com.example.fotori.dto.SubscriptionInfo;

public interface SubscriptionService {

    SubscriptionInfo getUserSubscription(String email);
}