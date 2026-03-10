package com.example.fotori.service;

import com.example.fotori.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PublicPhotographerService {

    List<PublicPhotographerItemResponse> getAllApproved();

    PublicPhotographerDetailResponse getDetail(Long photographerId);

    List<PublicPhotoPackageResponse> getPackages(Long photographerId);


    Page<PhotographerPublicDto> getPhotographers(
        int page,
        int size,
        String city,
        Integer minPrice,
        Integer maxPrice
    );

    Page<PublicReviewResponse> getPhotographerReviews(
        Long photographerId,
        int page,
        int size
    );

    List<PhotographerAvailabilityResponse> getAvailability(Long photographerId);

    Page<PortfolioImageResponse> getPortfolio(
        Long photographerId,
        int page,
        int size
    );
}