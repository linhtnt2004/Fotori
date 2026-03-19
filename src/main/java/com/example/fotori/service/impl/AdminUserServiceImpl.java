package com.example.fotori.service.impl;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.dto.ApiUserDetailResponse;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.*;
import com.example.fotori.service.AdminUserService;
import com.example.fotori.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final WishlistRepository wishlistRepository;
    private final NotificationRepository notificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final FirebaseService firebaseService;

    @Override
    public void updateUserStatus(Long userId, UserStatus status) {

        User user = userRepository
            .findById(userId)
            .orElseThrow(() ->
                             new BusinessException("USER_NOT_FOUND")
            );

        user.setStatus(status);

        userRepository.save(user);
    }

    @Override
    public ApiUserDetailResponse getUserDetail(Long userId) {

        User user = userRepository
            .findById(userId)
            .orElseThrow(() ->
                             new RuntimeException("USER_NOT_FOUND")
            );

        Long totalBookings =
            bookingRepository.countByUser(user);

        Double totalSpent =
            bookingRepository.getTotalSpent(user);

        String role = user.getRoles()
            .stream()
            .findFirst()
            .map(Role::getName)
            .orElse("UNKNOWN");

        return ApiUserDetailResponse.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .phone(user.getPhoneNumber())
            .avatarUrl(user.getAvatarUrl())
            .status(user.getStatus())
            .role(role)
            .createdAt(user.getCreatedAt())
            .totalBookings(totalBookings)
            .totalSpent(totalSpent)
            .build();
    }

    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        // Delete Firebase account first
        firebaseService.deleteFirebaseUser(user.getEmail());
        
        user.setStatus(UserStatus.DELETED);
        user.setDeletedAt(java.time.LocalDateTime.now());

        user.getRoles().clear();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserHard(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND"));

        // Delete Firebase account first
        firebaseService.deleteFirebaseUser(user.getEmail());
        
        // Delete associated data
        bookingRepository.deleteByUser(user);
        wishlistRepository.deleteByUser(user);
        notificationRepository.deleteByUser(user);
        refreshTokenRepository.deleteByUser(user);
        emailVerificationTokenRepository.deleteByUser(user);

        userRepository.delete(user);
    }
}
