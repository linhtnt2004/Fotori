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
                .orElseGet(() -> {
                    PhotographerProfile p = new PhotographerProfile();
                    p.setUser(user);
                    p.setApprovalStatus(ApprovalStatus.APPROVED);
                    return photographerProfileRepository.save(p);
                });

        return PhotographerProfileResponse.builder()
            .id(profile.getId())
            .userId(user.getId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .avatarUrl(user.getAvatarUrl())
            .coverUrl(user.getCoverUrl())
            .bio(profile.getBio())
            .city(profile.getCity())
            .equipment(profile.getEquipment())
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
                .orElseGet(() -> {
                    PhotographerProfile p = new PhotographerProfile();
                    p.setUser(user);
                    p.setApprovalStatus(ApprovalStatus.APPROVED);
                    return photographerProfileRepository.save(p);
                });

        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getExperienceYears() != null) profile.setExperienceYears(request.getExperienceYears());
        if (request.getCity() != null) profile.setCity(request.getCity());
        if (request.getEquipment() != null) profile.setEquipment(request.getEquipment());

        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getCoverUrl() != null) {
            user.setCoverUrl(request.getCoverUrl());
        }

        userRepository.save(user);
        photographerProfileRepository.save(profile);
    }

}
