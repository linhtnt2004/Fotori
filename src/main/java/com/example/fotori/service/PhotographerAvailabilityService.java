package com.example.fotori.service;

import com.example.fotori.dto.CreateAvailabilityRequest;
import com.example.fotori.dto.PhotographerAvailabilityResponse;

import java.util.List;

public interface PhotographerAvailabilityService {

    void createAvailability(String email, CreateAvailabilityRequest request);

    List<PhotographerAvailabilityResponse> getMyAvailability(String email);

    void deleteAvailability(String email, Long availabilityId);
}
