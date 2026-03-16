package com.example.fotori.service;

import com.example.fotori.dto.MySubscriptionResponse;
import com.example.fotori.dto.SubscriptionHistoryResponse;
import com.example.fotori.dto.SubscriptionPaymentHistoryResponse;
import com.example.fotori.dto.SubscriptionPlanResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubscriptionService {

    List<SubscriptionPlanResponse> getPlans();

    MySubscriptionResponse getMySubscription(Long userId);

    Page<SubscriptionPaymentHistoryResponse> getSubscriptionPayments(
        Long userId,
        int page,
        int size
    );

    List<SubscriptionHistoryResponse> getSubscriptionHistory(Long userId);

    void confirmSubscriptionPayment(Long paymentId);
}