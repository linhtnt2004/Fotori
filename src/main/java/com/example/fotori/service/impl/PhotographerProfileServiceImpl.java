package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.PhotographerProfileResponse;
import com.example.fotori.dto.UpdatePhotographerProfileRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PhotographerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PhotographerProfileServiceImpl
    implements PhotographerProfileService {

    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerProfileRepository;

    @Override
    public PhotographerProfileResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile profile =
            photographerProfileRepository.findByUser(user)
                .orElseThrow(() ->
                                 new BusinessException(
                                     "PHOTOGRAPHER_PROFILE_NOT_FOUND"
                                 )
                );

        return PhotographerProfileResponse.builder()
            .id(profile.getId())
            .userId(user.getId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .avatarUrl(user.getAvatarUrl())
            .bio(profile.getBio())
            .experienceYears(profile.getExperienceYears())
            .approvalStatus(profile.getApprovalStatus())
            .approvedAt(profile.getApprovedAt())
            .build();
    }

    @Override
    @Transactional
    public void updateMyProfile(
        String email,
        UpdatePhotographerProfileRequest request
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile profile =
            photographerProfileRepository.findByUser(user)
                .orElseThrow(() ->
                                 new BusinessException(
                                     "PHOTOGRAPHER_PROFILE_NOT_FOUND"
                                 )
                );

        if (profile.getApprovalStatus() == ApprovalStatus.APPROVED) {
            throw new BusinessException("PROFILE_ALREADY_APPROVED");
        }

        profile.setBio(request.getBio());
        profile.setExperienceYears(request.getExperienceYears());

        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

    }
}
