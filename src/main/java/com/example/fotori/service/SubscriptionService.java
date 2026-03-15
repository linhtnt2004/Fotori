package com.example.fotori.service;

import com.example.fotori.dto.SubscriptionPlanResponse;

import java.util.List;

public interface SubscriptionService {

    List<SubscriptionPlanResponse> getPlans();

}