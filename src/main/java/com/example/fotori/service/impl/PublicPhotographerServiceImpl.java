package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.*;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.PhotoPackage;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.PortfolioImage;
import com.example.fotori.model.Review;
import com.example.fotori.repository.*;
import com.example.fotori.service.PublicPhotographerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicPhotographerServiceImpl
    implements PublicPhotographerService {

    private final PhotographerProfileRepository photographerRepository;
    private final PhotoPackageRepository photoPackageRepository;
    private final ReviewRepository reviewRepository;
    private final PhotographerAvailabilityRepository availabilityRepository;
    private final PortfolioImageRepository portfolioImageRepository;

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
                         .durationMinutes(pkg.getDurationMinutes())
                         .build()
            )
            .toList();
    }

    @Override
    public Page<PhotographerPublicDto> getPhotographers(
        int page,
        int size,
        String city,
        Integer minPrice,
        Integer maxPrice
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PhotographerProfile> photographers =
            photographerRepository.findByApprovalStatus(
                ApprovalStatus.APPROVED,
                pageable
            );

        return photographers.map(p -> {

            Integer startingPrice =
                photoPackageRepository
                    .findByPhotographerProfileIdAndActiveTrue(p.getId())
                    .stream()
                    .map(PhotoPackage::getPrice)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            return PhotographerPublicDto.builder()
                .id(p.getId())
                .name(p.getUser().getFullName())
                .avatar(p.getUser().getAvatarUrl())
                .city(p.getCity())
                .bio(p.getBio())
                .experienceYears(p.getExperienceYears())
                .startingPrice(startingPrice)
                .build();
        });
    }

    @Override
    public Page<PublicReviewResponse> getPhotographerReviews(
        Long photographerId,
        int page,
        int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviews =
            reviewRepository.findByPhotographerProfileId(
                photographerId,
                pageable
            );

        return reviews.map(r ->
                               PublicReviewResponse.builder()
                                   .id(r.getId())
                                   .customerName(r.getCustomer().getFullName())
                                   .customerAvatar(r.getCustomer().getAvatarUrl())
                                   .rating(r.getRating())
                                   .comment(r.getComment())
                                   .createdAt(r.getCreatedAt())
                                   .build()
        );
    }

    @Override
    public List<PhotographerAvailabilityResponse> getAvailability(
        Long photographerId
    ) {

        PhotographerProfile profile =
            photographerRepository
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
            .map(a ->
                     PhotographerAvailabilityResponse.builder()
                         .id(a.getId())
                         .startTime(a.getStartTime())
                         .endTime(a.getEndTime())
                         .build()
            )
            .toList();
    }

    @Override
    public Page<PortfolioImageResponse> getPortfolio(
        Long photographerId,
        int page,
        int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<PortfolioImage> images =
            portfolioImageRepository.findByPhotographerId(
                photographerId,
                pageable
            );

        return images.map(img ->
                              PortfolioImageResponse.builder()
                                  .id(img.getId())
                                  .imageUrl(img.getImageUrl())
                                  .caption(img.getCaption())
                                  .build()
        );
    }

    @Override
    public List<TrendingPhotographerResponse> getTrendingPhotographers(
        int limit
    ) {

        Pageable pageable = PageRequest.of(0, limit);

        List<Object[]> results =
            reviewRepository.findTrendingPhotographers(pageable);

        return results.stream()
            .map(row -> {

                Long photographerId = (Long) row[0];
                Double avgRating = (Double) row[1];
                Long totalReviews = (Long) row[2];

                PhotographerProfile p =
                    photographerRepository.findById(photographerId)
                        .orElseThrow(() ->
                                         new BusinessException("PHOTOGRAPHER_NOT_FOUND")
                        );

                return TrendingPhotographerResponse.builder()
                    .id(p.getId())
                    .name(p.getUser().getFullName())
                    .avatar(p.getUser().getAvatarUrl())
                    .city(p.getCity())
                    .avgRating(avgRating)
                    .totalReviews(totalReviews)
                    .build();
            })
            .toList();
    }

    @Override
    public List<PublicPhotographerItemResponse> searchPhotographers(
        String keyword,
        String city
    ) {

        List<PhotographerProfile> photographers =
            photographerRepository.searchPhotographers(
                keyword,
                city
            );

        return photographers.stream()
            .map(this::toItemResponse)
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