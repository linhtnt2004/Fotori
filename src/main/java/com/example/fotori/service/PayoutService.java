package com.example.fotori.service;

import com.example.fotori.dto.PhotographerPayoutResponse;

public interface PayoutService {

    PhotographerPayoutResponse calculatePayout(Long bookingId);
}
