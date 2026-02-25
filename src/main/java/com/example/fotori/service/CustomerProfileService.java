package com.example.fotori.service;

import com.example.fotori.dto.CustomerProfileResponse;
import com.example.fotori.dto.UpdateCustomerProfileRequest;

public interface CustomerProfileService {
    CustomerProfileResponse getMyProfile(String email);

    void updateMyProfile(String email, UpdateCustomerProfileRequest request);
}
