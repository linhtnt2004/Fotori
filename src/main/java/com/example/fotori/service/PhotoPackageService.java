package com.example.fotori.service;

import com.example.fotori.dto.PhotoPackageCreateRequest;
import com.example.fotori.dto.PhotoPackageResponse;
import com.example.fotori.dto.UpdatePhotoPackageRequest;

import java.util.List;

public interface PhotoPackageService {
    void createPackage(String email, PhotoPackageCreateRequest request);

    List<PhotoPackageResponse> getAllActivePackage();

    PhotoPackageResponse updatePackage(
        String email,
        Long packageId,
        UpdatePhotoPackageRequest request
    );
}
