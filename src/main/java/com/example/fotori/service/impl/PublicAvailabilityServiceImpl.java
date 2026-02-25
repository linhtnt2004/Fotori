package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.PublicAvailabilityResponse;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.repository.PhotographerAvailabilityRepository;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.service.PublicAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicAvailabilityServiceImpl
    implements PublicAvailabilityService {

    private final PhotographerProfileRepository profileRepository;
    private final PhotographerAvailabilityRepository availabilityRepository;

    @Override
    public List<PublicAvailabilityResponse> getAvailableSlots(
        Long photographerId
    ) {

        PhotographerProfile profile =
            profileRepository
                .findByIdAndApprovalStatusAndDeletedAtIsNull(
                    photographerId,
                    ApprovalStatus.APPROVED
                )
                .orElseThrow(() ->
                                 new BusinessException("PHOTOGRAPHER_NOT_FOUND")
                );

        return availabilityRepository
            .findByPhotographerAndDeletedAtIsNull(profile)
            .stream()
            .map(slot ->
                     PublicAvailabilityResponse.builder()
                         .availabilityId(slot.getId())
                         .startTime(slot.getStartTime())
                         .endTime(slot.getEndTime())
                         .build()
            )
            .toList();
    }
}