package com.example.fotori.service;

import com.example.fotori.dto.PhotographerProfileResponse;
import com.example.fotori.dto.UpdatePhotographerProfileRequest;

public interface PhotographerProfileService {

    PhotographerProfileResponse getMyProfile(String email);

    void updateMyProfile(
        String email,
        UpdatePhotographerProfileRequest request
    );
}
