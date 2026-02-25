package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.PendingPhotographerResponse;
import com.example.fotori.dto.UpdateApprovalStatusRequest;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.AdminPhotographerService;
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

        if (request.getStatus() == ApprovalStatus.PENDING) {
            throw new BusinessException("INVALID_STATUS");
        }

        PhotographerProfile photographer = photographerRepository.findById(photographerId)
            .orElseThrow(() -> new BusinessException("PHOTOGRAPHER_NOT_FOUND"));

        if (photographer.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new BusinessException("PHOTOGRAPHER_ALREADY_PROCESSED");
        }

        User user = photographer.getUser();

        if (request.getStatus() == ApprovalStatus.APPROVED) {

            photographer.setApprovalStatus(ApprovalStatus.APPROVED);
            photographer.setApprovedAt(LocalDateTime.now());

            Role photographerRole = roleRepository.findByName("ROLE_PHOTOGRAPHER")
                .orElseThrow(() -> new BusinessException("ROLE_PHOTOGRAPHER_NOT_FOUND"));

            user.getRoles().add(photographerRole);

        } else if (request.getStatus() == ApprovalStatus.REJECTED) {

            photographer.setApprovalStatus(ApprovalStatus.REJECTED);
            photographer.setApprovedAt(null);

            user.getRoles().removeIf(role ->
                                         role.getName().equals("ROLE_PHOTOGRAPHER")
            );
        }

        photographerRepository.save(photographer);
        userRepository.save(user);
    }
}
