package com.example.fotori.service.impl;

import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.dto.MySubscriptionResponse;
import com.example.fotori.dto.SubscriptionHistoryResponse;
import com.example.fotori.dto.SubscriptionPaymentHistoryResponse;
import com.example.fotori.dto.SubscriptionPlanResponse;
import com.example.fotori.dto.admin.AdminPaymentDTO;
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
import java.util.stream.Collectors;

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
                .orElse(null);

        if (photographer == null) return null;

        PhotographerSubscription subscription =
            subscriptionRepository
                .findFirstByPhotographer_IdOrderByEndDateDesc(
                    photographer.getId()
                )
                .orElse(null);

        if (subscription == null) return null;

        long daysRemaining =
            java.time.Duration.between(
                LocalDateTime.now(),
                subscription.getEndDate()
            ).toDays();

        return MySubscriptionResponse.builder()
            .id(subscription.getId())
            .planName(subscription.getPlan().getName())
            .startDate(subscription.getStartDate())
            .endDate(subscription.getEndDate())
            .daysRemaining(daysRemaining)
            .active(subscription.getEndDate().isAfter(LocalDateTime.now()))
            .build();
    }

    @Override
    public MySubscriptionResponse getSubscriptionByPhotographerId(Long photographerId) {
        PhotographerSubscription subscription =
            subscriptionRepository
                .findFirstByPhotographer_IdOrderByEndDateDesc(photographerId)
                .orElse(null);

        if (subscription == null) return null;

        long daysRemaining =
            java.time.Duration.between(
                LocalDateTime.now(),
                subscription.getEndDate()
            ).toDays();

        return MySubscriptionResponse.builder()
            .id(subscription.getId())
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
                .orElse(null);

        if (photographer == null) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Payment> payments =
            paymentRepository.findByPhotographer_Id(
                photographer.getId(),
                pageable
            );

        return payments.map(payment ->
                                 SubscriptionPaymentHistoryResponse.builder()
                                     .planName(payment.getSubscriptionPlan() != null ? payment.getSubscriptionPlan().getName() : "Nâng cấp gói")
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
                .paymentId(paymentId)
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    public List<AdminPaymentDTO> getAllSubscriptionPayments() {
        return paymentRepository.findAllBySubscriptionPlanIsNotNullOrderByCreatedAtDesc()
            .stream()
            .map(p -> {
                AdminPaymentDTO.AdminPaymentDTOBuilder builder = AdminPaymentDTO.builder()
                    .id(p.getId())
                    .amount(p.getAmount())
                    .method(p.getMethod() != null ? p.getMethod().name() : "Thủ công")
                    .transactionId(p.getTransactionId())
                    .status(p.getStatus() != null ? p.getStatus().name() : "PENDING")
                    .createdAt(p.getCreatedAt());

                if (p.getSubscriptionPlan() != null) {
                    builder.planId(p.getSubscriptionPlan().getId())
                        .planName(p.getSubscriptionPlan().getName())
                        .photographerId(p.getPhotographer() != null ? p.getPhotographer().getId() : null)
                        .payerName(p.getPhotographer() != null && p.getPhotographer().getUser() != null
                                        ? p.getPhotographer().getUser().getFullName() : "Thợ ảnh");
                }

                return builder.build();
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSubscriptionPayment(Long paymentId) {
        subscriptionRepository.deleteByPaymentId(paymentId);
        paymentRepository.deleteById(paymentId);
    }

    @Override
    @Transactional
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }
}