package com.example.fotori.service.impl;

import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.dto.MySubscriptionResponse;
import com.example.fotori.dto.SubscriptionHistoryResponse;
import com.example.fotori.dto.SubscriptionPaymentHistoryResponse;
import com.example.fotori.dto.SubscriptionPlanResponse;
import com.example.fotori.model.Payment;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.PhotographerSubscription;
import com.example.fotori.model.SubscriptionPlan;
import com.example.fotori.repository.*;
import com.example.fotori.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl
    implements SubscriptionService {

    private final PhotographerRepository photographerRepository;
    private final SubscriptionPlanRepository planRepository;
    private final PhotographerSubscriptionRepository subscriptionRepository;
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

    @Override
    public MySubscriptionResponse getMySubscription(Long userId) {

        PhotographerProfile photographer =
            photographerRepository.findByUserId(userId)
                .orElseThrow(() ->
                                 new RuntimeException("PHOTOGRAPHER_NOT_FOUND")
                );

        PhotographerSubscription subscription =
            subscriptionRepository
                .findFirstByPhotographer_IdOrderByEndDateDesc(
                    photographer.getId()
                )
                .orElseThrow(() ->
                                 new RuntimeException("NO_SUBSCRIPTION")
                );

        long daysRemaining =
            java.time.Duration.between(
                LocalDateTime.now(),
                subscription.getEndDate()
            ).toDays();

        return MySubscriptionResponse.builder()
            .planName(subscription.getPlan().getName())
            .startDate(subscription.getStartDate())
            .endDate(subscription.getEndDate())
            .daysRemaining(daysRemaining)
            .active(subscription.getEndDate().isAfter(LocalDateTime.now()))
            .build();
    }

    @Override
    public Page<SubscriptionPaymentHistoryResponse> getSubscriptionPayments(
        Long userId,
        int page,
        int size
    ) {

        PhotographerProfile photographer =
            photographerRepository.findByUserId(userId)
                .orElseThrow(() ->
                                 new RuntimeException("PHOTOGRAPHER_NOT_FOUND")
                );

        Pageable pageable = PageRequest.of(page, size);

        Page<Payment> payments =
            paymentRepository.findByPhotographer_Id(
                photographer.getId(),
                pageable
            );

        return payments.map(payment ->
                                SubscriptionPaymentHistoryResponse.builder()
                                    .planName(payment.getSubscriptionPlan().getName())
                                    .amount(payment.getAmount())
                                    .status(payment.getStatus().name())
                                    .transactionId(payment.getTransactionId())
                                    .createdAt(payment.getCreatedAt())
                                    .build()
        );
    }

    @Override
    public List<SubscriptionHistoryResponse> getSubscriptionHistory(Long userId) {

        PhotographerProfile photographer =
            photographerRepository.findByUserId(userId)
                .orElseThrow(() ->
                                 new RuntimeException("PHOTOGRAPHER_NOT_FOUND"));

        List<PhotographerSubscription> subscriptions =
            subscriptionRepository
                .findByPhotographer_IdOrderByStartDateDesc(
                    photographer.getId()
                );

        return subscriptions.stream()
            .map(s -> SubscriptionHistoryResponse.builder()
                .planName(s.getPlan().getName())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .active(s.getActive())
                .build())
            .toList();
    }

    @Override
    @Transactional
    public void confirmSubscriptionPayment(Long paymentId) {

        Payment payment = paymentRepository
            .findById(paymentId)
            .orElseThrow(() ->
                             new RuntimeException("PAYMENT_NOT_FOUND"));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("PAYMENT_ALREADY_CONFIRMED");
        }

        SubscriptionPlan plan = payment.getSubscriptionPlan();

        PhotographerProfile photographer = payment.getPhotographer();

        if (plan == null || photographer == null) {
            throw new RuntimeException("INVALID_SUBSCRIPTION_PAYMENT");
        }

        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(plan.getDurationDays());

        PhotographerSubscription subscription =
            PhotographerSubscription.builder()
                .photographer(photographer)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .active(true)
                .build();

        subscriptionRepository.save(subscription);
    }
}