package com.example.fotori.service;

import com.example.fotori.dto.AdminPayoutItemResponse;
import com.example.fotori.dto.PhotographerPayoutResponse;

import java.util.List;

public interface PayoutService {

    PhotographerPayoutResponse calculatePayout(Long bookingId);

    List<AdminPayoutItemResponse> getPendingPayouts();

    void markAsTransferred(Long bookingId);
    
    void confirmReceipt(Long bookingId);

    List<PhotographerPayoutResponse> getPayoutHistory(String email);
    
    List<AdminPayoutItemResponse> getAllPayouts();
}
