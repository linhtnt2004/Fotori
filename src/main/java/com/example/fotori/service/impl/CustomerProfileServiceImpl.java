package com.example.fotori.service.impl;

import com.example.fotori.dto.CustomerProfileResponse;
import com.example.fotori.dto.UpdateCustomerProfileRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.User;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final UserRepository userRepository;

    @Override
    public CustomerProfileResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        return CustomerProfileResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .phoneNumber(user.getPhoneNumber())
            .gender(user.getGender())
            .birthDate(user.getBirthDate())
            .avatarUrl(user.getAvatarUrl())
            .createdAt(user.getCreatedAt())
            .build();
    }

    @Override
    @Transactional
    public void updateMyProfile(
        String email,
        UpdateCustomerProfileRequest request
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setAvatarUrl(request.getAvatarUrl());

    }
}
