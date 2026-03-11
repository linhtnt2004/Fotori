package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.dto.admin.*;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.repository.RoleRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerProfileRepository;
    private final BookingRepository bookingRepository;
    private final RoleRepository roleRepository;

    @Override
    public AdminStatsDTO getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalPhotographers = photographerProfileRepository.count();
        
        // Count customers (users with ROLE_CUSTOMER)
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER").orElse(null);
        long totalCustomers = 0;
        if (customerRole != null) {
            totalCustomers = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(customerRole))
                .count();
        }

        long totalBookings = bookingRepository.count();

        // Calculate total revenue (sum of all final prices for PAID bookings)
        // For now we just sum all package prices of completed/accepted bookings
        // User asked to leave "transactions/payment" part alone, so we just sum what we can or return 0
        double totalRevenue = bookingRepository.findAll().stream()
            .filter(b -> b.getPaymentStatus() == PaymentStatus.PAID)
            .mapToDouble(b -> b.getFinalPrice() != null ? b.getFinalPrice() : 0.0)
            .sum();

        return AdminStatsDTO.builder()
            .totalUsers(totalUsers)
            .totalPhotographers(totalPhotographers)
            .totalCustomers(totalCustomers)
            .totalBookings(totalBookings)
            .totalRevenue(totalRevenue)
            .build();
    }

    @Override
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(u -> AdminUserDTO.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .status(u.getStatus().name())
                .createdAt(u.getCreatedAt())
                .roles(u.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<AdminBookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(b -> AdminBookingDTO.builder()
                .id(b.getId())
                .customerName(b.getUser().getFullName())
                .customerEmail(b.getUser().getEmail())
                .photographerName(b.getPhotographer().getUser().getFullName())
                .packageTitle(b.getPhotoPackage().getTitle())
                .packagePrice(b.getFinalPrice())
                .startTime(b.getStartTime())
                .endTime(b.getEndTime())
                .status(b.getStatus().name())
                .createdAt(b.getCreatedAt())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<AdminPhotographerDTO> getAllPhotographers() {
        return photographerProfileRepository.findAll().stream()
            .filter(p -> p.getDeletedAt() == null)
            .map(p -> AdminPhotographerDTO.builder()
                .id(p.getId())
                .userId(p.getUser().getId())
                .fullName(p.getUser().getFullName())
                .email(p.getUser().getEmail())
                .approvalStatus(p.getApprovalStatus().name())
                .bio(p.getBio())
                .experienceYears(p.getExperienceYears())
                .city(p.getCity())
                .createdAt(p.getCreatedAt())
                .approvedAt(p.getApprovedAt())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updatePhotographerStatus(Long photographerId, ApprovalStatus status) {
        PhotographerProfile profile = photographerProfileRepository.findById(photographerId)
            .orElseThrow(() -> new BusinessException("PHOTOGRAPHER_NOT_FOUND"));

        profile.setApprovalStatus(status);
        if (status == ApprovalStatus.APPROVED) {
            profile.setApprovedAt(LocalDateTime.now());
            // Need to update actual user status to active if requested?
            User user = profile.getUser();
            user.setStatus(com.example.fotori.common.enums.UserStatus.ACTIVE);
            userRepository.save(user);
        }

        photographerProfileRepository.save(profile);
    }
}
