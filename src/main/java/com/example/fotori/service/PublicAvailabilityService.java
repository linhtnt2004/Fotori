package com.example.fotori.service;

import com.example.fotori.dto.PublicAvailabilityResponse;

import java.util.List;

public interface PublicAvailabilityService {

    List<PublicAvailabilityResponse> getAvailableSlots(
        Long photographerId
    );
}