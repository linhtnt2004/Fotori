package com.example.fotori.service.impl;

import com.example.fotori.dto.SubscriptionPlanResponse;
import com.example.fotori.model.SubscriptionPlan;
import com.example.fotori.repository.PaymentRepository;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.SubscriptionPlanRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl
    implements SubscriptionService {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final SubscriptionPlanRepository planRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public List<SubscriptionPlanResponse> getPlans() {

        List<SubscriptionPlan> plans = planRepository.findAll();

        return plans.stream()
            .map(plan -> SubscriptionPlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .price(plan.getPrice())
                .durationDays(plan.getDurationDays())
                .commissionPercent(plan.getCommissionPercent())
                .maxPackages(plan.getMaxPackages())
                .aiSuggest(plan.getAiSuggest())
                .priorityListing(plan.getPriorityListing())
                .unlimitedChat(plan.getUnlimitedChat())
                .analytics(plan.getAnalytics())
                .marketingTools(plan.getMarketingTools())
                .build())
            .toList();
    }

}