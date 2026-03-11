package com.example.fotori.service.impl;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.dto.ApiUserDetailResponse;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

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
}
