package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.PendingPhotographerResponse;
import com.example.fotori.dto.UpdateApprovalStatusRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.*;
import com.example.fotori.repository.PaymentRepository;
import com.example.fotori.service.AdminPhotographerService;
import com.example.fotori.service.EmailService;
import com.example.fotori.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPhotographerServiceImpl implements AdminPhotographerService {

    private final PhotographerRepository photographerRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PhotoPackageRepository photoPackageRepository;
    private final PortfolioImageRepository portfolioImageRepository;
    private final PhotographerSubscriptionRepository photographerSubscriptionRepository;
    private final PhotographerAvailabilityRepository photographerAvailabilityRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final FirebaseService firebaseService;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public List<PendingPhotographerResponse> getPendingPhotographers() {

        List<PhotographerProfile> photographers =
            photographerRepository.findByApprovalStatus(ApprovalStatus.PENDING);

        return photographers.stream()
            .map(p -> new PendingPhotographerResponse(
                p.getId(),
                p.getUser().getId(),
                p.getUser().getEmail(),
                p.getUser().getFullName(),
                p.getApprovalStatus().name()
            ))
            .toList();
    }

    @Override
    @Transactional
    public void updatePhotographerStatus(Long photographerId, UpdateApprovalStatusRequest request) {

        if (request.getStatus() == null) {
            throw new BusinessException("STATUS_REQUIRED");
        }

        PhotographerProfile photographer = photographerRepository.findById(photographerId)
            .orElseThrow(() -> new BusinessException("PHOTOGRAPHER_NOT_FOUND"));

        User user = photographer.getUser();

        if (request.getStatus() == ApprovalStatus.APPROVED) {

            photographer.setApprovalStatus(ApprovalStatus.APPROVED);
            photographer.setApprovedAt(LocalDateTime.now());

            Role photographerRole = roleRepository.findByName("ROLE_PHOTOGRAPHER")
                .orElseThrow(() -> new BusinessException("ROLE_PHOTOGRAPHER_NOT_FOUND"));

            user.getRoles().add(photographerRole);

            // Notify photographer about approval
            try {
                emailService.sendPhotographerApprovalNotification(user.getEmail(), user.getFullName());
            } catch (Exception e) {
                // Log and continue, don't fail the approval transaction
            }

        } else if (request.getStatus() == ApprovalStatus.REJECTED) {

            photographer.setApprovalStatus(ApprovalStatus.REJECTED);
            photographer.setApprovedAt(null);

            user.getRoles().removeIf(role ->
                                         role.getName().equals("ROLE_PHOTOGRAPHER")
            );
        } else if (request.getStatus() == ApprovalStatus.PENDING) {

            photographer.setApprovalStatus(ApprovalStatus.PENDING);
            photographer.setApprovedAt(null);

            user.getRoles().removeIf(role ->
                                         role.getName().equals("ROLE_PHOTOGRAPHER")
            );
        }

        photographerRepository.save(photographer);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deletePhotographer(Long id) {
        PhotographerProfile photographer = photographerRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Photographer not found"));

        photographer.setDeletedAt(LocalDateTime.now());

        User user = photographer.getUser();
        user.getRoles().removeIf(role ->
                                     role.getName().equals("ROLE_PHOTOGRAPHER")
        );

        photographerRepository.save(photographer);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deletePhotographerHard(Long id) {
        PhotographerProfile photographer = photographerRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Photographer not found"));

        // Delete associated data
        photoPackageRepository.deleteByPhotographerProfile(photographer);
        portfolioImageRepository.deleteByPhotographer(photographer);
        photographerSubscriptionRepository.deleteByPhotographer(photographer);
        photographerAvailabilityRepository.deleteByPhotographer(photographer);
        
        // Delete payments related to photographer (subs, etc)
        paymentRepository.deleteByPhotographer_Id(photographer.getId());
        
        // Note: some payments might be linked via booking.
        // We delete reviews before bookings because reviews depend on bookings
        reviewRepository.deleteByPhotographer(photographer);
        bookingRepository.deleteByPhotographer(photographer);

        // Delete from Firebase Auth
        if (photographer.getUser() != null) {
            firebaseService.deleteFirebaseUser(photographer.getUser().getEmail());
        }

        photographerRepository.delete(photographer);
    }

    @Override
    @Transactional
    public void updateCoverImage(Long photographerId, String coverUrl) {
        PhotographerProfile photographer = photographerRepository.findById(photographerId)
            .orElseThrow(() -> new BusinessException("Photographer not found"));

        User user = photographer.getUser();
        user.setCoverUrl(coverUrl);
        userRepository.save(user);
    }
}
