package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.MatchingRequestDto;
import com.example.fotori.dto.PhotographerMatchDto;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.repository.PhotographerAvailabilityRepository;
import com.example.fotori.repository.PhotoPackageRepository;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.service.AIMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIMatchingServiceImpl implements AIMatchingService {

    private final PhotographerProfileRepository photographerRepo;
    private final PhotoPackageRepository packageRepo;
    private final PhotographerAvailabilityRepository availabilityRepo;

    @Override
    public List<PhotographerMatchDto> findMatchingPhotographers(MatchingRequestDto request) {
        List<PhotographerProfile> photographers =
            photographerRepo.findByApprovalStatusAndDeletedAtIsNull(ApprovalStatus.APPROVED);

        int limit = request.getLimit() != null ? request.getLimit() : 10;

        return photographers.stream()
            .map(p -> calculateMatchScore(p, request))
            .sorted(Comparator.comparingInt(PhotographerMatchDto::getMatchScore).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    private PhotographerMatchDto calculateMatchScore(
            PhotographerProfile photographer,
            MatchingRequestDto request) {

        List<String> matchReasons = new ArrayList<>();
        int conceptMatch  = 0;
        int budgetMatch   = 0;
        int locationMatch = 0;
        int ratingScore   = 0;

        // 1. CONCEPT MATCHING (40%)
        if (request.getConceptIds() != null && !request.getConceptIds().isEmpty()) {
            List<Long> photographerConceptIds =
                packageRepo.findConceptIdsByPhotographerId(photographer.getId());

            long matchCount = request.getConceptIds().stream()
                .filter(photographerConceptIds::contains)
                .count();

            if (matchCount > 0) {
                conceptMatch = (int) Math.min(100,
                    (matchCount * 100.0 / request.getConceptIds().size()));
                matchReasons.add("Phù hợp " + matchCount + " concept bạn yêu cầu");
            }
        }

        // 2. BUDGET MATCHING (30%)
        Integer minPrice = packageRepo.findMinPriceByPhotographerId(photographer.getId());
        if (minPrice != null && request.getBudgetMin() != null
                && request.getBudgetMax() != null) {
            if (minPrice >= request.getBudgetMin() && minPrice <= request.getBudgetMax()) {
                budgetMatch = 100;
                matchReasons.add("Giá phù hợp với ngân sách của bạn");
            } else if (minPrice <= request.getBudgetMax() * 1.2) {
                budgetMatch = 70;
                matchReasons.add("Giá gần với ngân sách mong muốn");
            } else if (minPrice <= request.getBudgetMax() * 1.5) {
                budgetMatch = 40;
            }
        }

        // 3. LOCATION MATCHING (20%)
        if (request.getCity() != null && photographer.getCity() != null) {
            if (photographer.getCity().equalsIgnoreCase(request.getCity())) {
                locationMatch = 100;
                matchReasons.add("Cùng thành phố: " + request.getCity());
            } else {
                locationMatch = 30;
            }
        }

        // 4. RATING SCORE (10%)
        if (photographer.getAverageRating() != null) {
            ratingScore = (int) (photographer.getAverageRating() * 20);
            if (photographer.getAverageRating() >= 4.5) {
                matchReasons.add("Đánh giá xuất sắc ("
                    + photographer.getAverageRating() + "★)");
            }
        }

        // WEIGHTED TOTAL
        int matchScore = Math.round(
            conceptMatch  * 0.40f +
            budgetMatch   * 0.30f +
            locationMatch * 0.20f +
            ratingScore   * 0.10f
        );

        // AVAILABILITY CHECK
        boolean isAvailable = true;
        if (request.getBookingDate() != null) {
            isAvailable = availabilityRepo.existsByPhotographerIdAndBookingDate(
                photographer.getId(), request.getBookingDate());
            if (isAvailable) {
                matchReasons.add("Có lịch trống vào ngày bạn chọn");
            }
        }

        List<String> conceptNames =
            packageRepo.findConceptNamesByPhotographerId(photographer.getId());
        Integer maxPrice = packageRepo.findMaxPriceByPhotographerId(photographer.getId());

        return PhotographerMatchDto.builder()
            .photographerId(photographer.getId())
            .fullName(photographer.getUser().getFullName())
            .avatarUrl(photographer.getUser().getAvatarUrl())
            .city(photographer.getCity())
            .bio(photographer.getBio())
            .experienceYears(photographer.getExperienceYears())
            .averageRating(photographer.getAverageRating())
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .conceptNames(conceptNames)
            .matchScore(matchScore)
            .conceptMatch(conceptMatch)
            .budgetMatch(budgetMatch)
            .locationMatch(locationMatch)
            .ratingScore(ratingScore)
            .matchReasons(matchReasons)
            .matchLabel(getMatchLabel(matchScore))
            .isAvailable(isAvailable)
            .build();
    }

    private String getMatchLabel(int score) {
        if (score >= 80) return "Rất phù hợp";
        if (score >= 60) return "Phù hợp";
        if (score >= 40) return "Có thể phù hợp";
        return "Ít phù hợp";
    }
}