package com.example.fotori.service.impl;

import com.example.fotori.dto.CreateAvailabilityRequest;
import com.example.fotori.dto.PhotographerAvailabilityResponse;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.PhotographerAvailability;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotographerAvailabilityRepository;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PhotographerAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotographerAvailabilityServiceImpl
    implements PhotographerAvailabilityService {

    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerProfileRepository;
    private final PhotographerAvailabilityRepository availabilityRepository;

    @Override
    @Transactional
    public void createAvailability(
        String email,
        CreateAvailabilityRequest request
    ) {

        if (request.getStartTime() == null ||
            request.getEndTime() == null) {
            throw new BusinessException("INVALID_TIME_RANGE");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException("START_AFTER_END");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile profile =
            photographerProfileRepository.findByUser(user)
                .orElseThrow(() ->
                                 new BusinessException("PROFILE_NOT_FOUND")
                );

        boolean overlapped =
            availabilityRepository
                .existsByPhotographerAndStartTimeLessThanAndEndTimeGreaterThanAndDeletedAtIsNull(
                    profile,
                    request.getEndTime(),
                    request.getStartTime()
                );

        if (overlapped) {
            throw new BusinessException("TIME_SLOT_OVERLAPPED");
        }

        PhotographerAvailability availability =
            PhotographerAvailability.builder()
                .photographer(profile)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        availabilityRepository.save(availability);
    }

    @Override
    public List<PhotographerAvailabilityResponse> getMyAvailability(
        String email
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile profile =
            photographerProfileRepository.findByUser(user)
                .orElseThrow(() ->
                                 new BusinessException("PROFILE_NOT_FOUND")
                );

        return availabilityRepository
            .findByPhotographerAndDeletedAtIsNull(profile)
            .stream()
            .map(a ->
                     PhotographerAvailabilityResponse.builder()
                         .id(a.getId())
                         .startTime(a.getStartTime())
                         .endTime(a.getEndTime())
                         .build()
            )
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAvailability(String email, Long availabilityId) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile profile =
            photographerProfileRepository.findByUser(user)
                .orElseThrow(() ->
                                 new BusinessException("PROFILE_NOT_FOUND")
                );

        PhotographerAvailability availability =
            availabilityRepository.findById(availabilityId)
                .orElseThrow(() ->
                                 new BusinessException("AVAILABILITY_NOT_FOUND")
                );

        if (!availability.getPhotographer().getId()
            .equals(profile.getId())) {
            throw new BusinessException("NOT_YOUR_AVAILABILITY");
        }

        availability.setDeletedAt(LocalDateTime.now());
    }
}