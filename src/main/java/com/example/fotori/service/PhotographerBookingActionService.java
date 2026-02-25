package com.example.fotori.service;

import com.example.fotori.dto.UpdateBookingStatusRequest;

public interface PhotographerBookingActionService {

    void updateStatus(String photographerEmail, Long bookingId, UpdateBookingStatusRequest request);
}
