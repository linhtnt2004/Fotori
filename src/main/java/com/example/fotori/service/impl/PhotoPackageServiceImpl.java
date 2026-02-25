package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.PhotoPackageCreateRequest;
import com.example.fotori.dto.PhotoPackageResponse;
import com.example.fotori.model.PhotoPackage;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotoPackageRepository;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.PhotoPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoPackageServiceImpl implements PhotoPackageService {

    private final PhotoPackageRepository photoPackageRepository;
    private final PhotographerRepository photographerRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createPackage(String email, PhotoPackageCreateRequest request) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Not a photographer"));

        if (photographer.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new RuntimeException("Photographer not approved yet!");
        }

        PhotoPackage photoPackage = PhotoPackage.builder()
            .photographerProfile(photographer)
            .title(request.getTitle())
            .description(request.getDescription())
            .price(request.getPrice())
            .durationMinutes(request.getDurationMinutes())
            .active(true)
            .build();

        photoPackageRepository.save(photoPackage);
    }

    @Override
    @Transactional
    public List<PhotoPackageResponse> getAllActivePackage() {
        return photoPackageRepository.findByActiveTrue()
            .stream()
            .map(pkg -> new PhotoPackageResponse(
                pkg.getId(),
                pkg.getTitle(),
                pkg.getDescription(),
                pkg.getPrice(),
                pkg.getDurationMinutes(),
                pkg.getPhotographerProfile()
                    .getUser()
                    .getFullName()
            ))
            .toList();
    }
}
