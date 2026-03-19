package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.PhotoPackageCreateRequest;
import com.example.fotori.dto.PhotoPackageResponse;
import com.example.fotori.dto.UpdatePhotoPackageRequest;
import com.example.fotori.exception.BusinessException;
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
    private final com.example.fotori.repository.PhotographerSubscriptionRepository photographerSubscriptionRepository;

    @Override
    @Transactional
    public void createPackage(String email, PhotoPackageCreateRequest request) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseGet(() -> {
                    PhotographerProfile p = new PhotographerProfile();
                    p.setUser(user);
                    p.setApprovalStatus(ApprovalStatus.APPROVED);
                    return photographerRepository.save(p);
                });

        if (photographer.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new RuntimeException("Photographer not approved yet!");
        }

        // Enforce subscription maxPackages limit if photographer has an active subscription
        photographerSubscriptionRepository.findFirstByPhotographerAndActiveTrue(photographer)
            .ifPresent(sub -> {
                Integer max = sub.getPlan() != null ? sub.getPlan().getMaxPackages() : null;
                if (max != null) {
                    int current = photoPackageRepository.findByPhotographerProfileAndActiveTrue(photographer).size();
                    if (current >= max) {
                        throw new BusinessException("PACKAGE_LIMIT_REACHED");
                    }
                }
            });

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

    @Override
    @Transactional
    public List<PhotoPackageResponse> getMyPackages(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile profile = photographerRepository.findByUser(user)
            .orElseGet(() -> {
                PhotographerProfile p = new PhotographerProfile();
                p.setUser(user);
                p.setApprovalStatus(ApprovalStatus.APPROVED);
                return photographerRepository.save(p);
            });

        return photoPackageRepository.findByPhotographerProfileAndActiveTrue(profile)
            .stream()
            .map(pkg -> new PhotoPackageResponse(
                pkg.getId(),
                pkg.getTitle(),
                pkg.getDescription(),
                pkg.getPrice(),
                pkg.getDurationMinutes(),
                user.getFullName()
            ))
            .toList();
    }

    @Override
    @Transactional
    public PhotoPackageResponse updatePackage(
        String email,
        Long packageId,
        UpdatePhotoPackageRequest request
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile profile =
            photographerRepository.findByUser(user)
                .orElseGet(() -> {
                    PhotographerProfile p = new PhotographerProfile();
                    p.setUser(user);
                    p.setApprovalStatus(ApprovalStatus.APPROVED);
                    return photographerRepository.save(p);
                });

        PhotoPackage photoPackage =
            photoPackageRepository.findById(packageId)
                .orElseThrow(() ->
                                 new BusinessException("PACKAGE_NOT_FOUND")
                );

        if (!photoPackage.getPhotographerProfile().getId()
            .equals(profile.getId())) {

            throw new BusinessException("NOT_YOUR_PACKAGE");
        }

        photoPackage.setTitle(request.getTitle());
        photoPackage.setDescription(request.getDescription());
        photoPackage.setDurationMinutes(request.getDurationMinutes());
        photoPackage.setPrice(request.getPrice());

        photoPackageRepository.save(photoPackage);

        return PhotoPackageResponse.builder()
            .id(photoPackage.getId())
            .title(photoPackage.getTitle())
            .description(photoPackage.getDescription())
            .durationMinutes(photoPackage.getDurationMinutes())
            .price(photoPackage.getPrice())
            .build();
    }

    @Override
    @Transactional
    public void deletePackage(
        String email,
        Long packageId
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        PhotographerProfile profile =
            photographerRepository.findByUser(user)
                .orElseGet(() -> {
                    PhotographerProfile p = new PhotographerProfile();
                    p.setUser(user);
                    p.setApprovalStatus(ApprovalStatus.APPROVED);
                    return photographerRepository.save(p);
                });

        PhotoPackage photoPackage =
            photoPackageRepository.findById(packageId)
                .orElseThrow(() ->
                                 new BusinessException("PACKAGE_NOT_FOUND")
                );

        if (!photoPackage.getPhotographerProfile().getId()
            .equals(profile.getId())) {

            throw new BusinessException("NOT_YOUR_PACKAGE");
        }

        photoPackage.setActive(false);

        photoPackageRepository.save(photoPackage);
    }
}
