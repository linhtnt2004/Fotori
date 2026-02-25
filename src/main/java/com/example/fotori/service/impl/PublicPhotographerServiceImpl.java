package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.PublicPhotoPackageResponse;
import com.example.fotori.dto.PublicPhotographerDetailResponse;
import com.example.fotori.dto.PublicPhotographerItemResponse;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.repository.PhotoPackageRepository;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.service.PublicPhotographerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicPhotographerServiceImpl
    implements PublicPhotographerService {

    private final PhotographerProfileRepository photographerRepository;
    private final PhotoPackageRepository photoPackageRepository;

    @Override
    public List<PublicPhotographerItemResponse> getAllApproved() {

        return photographerRepository
            .findByApprovalStatusAndDeletedAtIsNull(
                ApprovalStatus.APPROVED
            )
            .stream()
            .map(this::toItemResponse)
            .toList();
    }

    @Override
    public PublicPhotographerDetailResponse getDetail(Long photographerId) {

        PhotographerProfile profile =
            photographerRepository
                .findByIdAndApprovalStatusAndDeletedAtIsNull(
                    photographerId,
                    ApprovalStatus.APPROVED
                )
                .orElseThrow(() ->
                                 new BusinessException(
                                     "PHOTOGRAPHER_NOT_FOUND"
                                 )
                );

        return PublicPhotographerDetailResponse.builder()
            .id(profile.getId())
            .fullName(profile.getUser().getFullName())
            .avatarUrl(profile.getUser().getAvatarUrl())
            .bio(profile.getBio())
            .experienceYears(profile.getExperienceYears())
            .build();
    }

    @Override
    public List<PublicPhotoPackageResponse> getPackages(Long photographerId) {

        PhotographerProfile profile =
            photographerRepository
                .findByIdAndApprovalStatusAndDeletedAtIsNull(
                    photographerId,
                    ApprovalStatus.APPROVED
                )
                .orElseThrow(() ->
                                 new BusinessException("PHOTOGRAPHER_NOT_FOUND")
                );

        return photoPackageRepository
            .findByPhotographerProfileAndActiveTrue(profile)
            .stream()
            .map(pkg ->
                     PublicPhotoPackageResponse.builder()
                         .packageId(pkg.getId())
                         .title(pkg.getTitle())
                         .description(pkg.getDescription())
                         .price(pkg.getPrice())
                         .build()
            )
            .toList();
    }

    private PublicPhotographerItemResponse toItemResponse(
        PhotographerProfile profile
    ) {
        return PublicPhotographerItemResponse.builder()
            .id(profile.getId())
            .fullName(profile.getUser().getFullName())
            .avatarUrl(profile.getUser().getAvatarUrl())
            .bio(profile.getBio())
            .experienceYears(profile.getExperienceYears())
            .build();
    }
}