package com.example.fotori.service;

import com.example.fotori.dto.PhotographerPublicDto;
import com.example.fotori.dto.PublicPhotoPackageResponse;
import com.example.fotori.dto.PublicPhotographerDetailResponse;
import com.example.fotori.dto.PublicPhotographerItemResponse;
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
}