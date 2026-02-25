package com.example.fotori.service;

import com.example.fotori.dto.PublicPhotoPackageResponse;
import com.example.fotori.dto.PublicPhotographerDetailResponse;
import com.example.fotori.dto.PublicPhotographerItemResponse;

import java.util.List;

public interface PublicPhotographerService {

    List<PublicPhotographerItemResponse> getAllApproved();

    PublicPhotographerDetailResponse getDetail(Long photographerId);

    List<PublicPhotoPackageResponse> getPackages(Long photographerId);
}