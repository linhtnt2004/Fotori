package com.example.fotori.service.impl;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.exception.BusinessException;
import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.dto.admin.*;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Role;
import com.example.fotori.model.User;
import com.example.fotori.repository.BookingRepository;
import com.example.fotori.repository.PaymentRepository;
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
    private final PaymentRepository paymentRepository;
    private final com.example.fotori.repository.ReviewRepository reviewRepository;
    private final com.example.fotori.repository.PhotographerSubscriptionRepository photographerSubscriptionRepository;

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

        // Calculate total revenue (sum of all platform cut for PAID payments)
        double totalRevenue = paymentRepository.findAll().stream()
            .filter(p -> p.getStatus() == PaymentStatus.PAID)
            .mapToDouble(p -> p.getPlatformRevenue() != null ? p.getPlatformRevenue() : 0.0)
            .sum();

        // Review stats
        long totalReviews = reviewRepository.count();
        double avgRating = 0.0;
        if (totalReviews > 0) {
            avgRating = reviewRepository.findAll().stream()
                .mapToInt(com.example.fotori.model.Review::getRating)
                .average()
                .orElse(0.0);
        }

        return AdminStatsDTO.builder()
            .totalUsers(totalUsers)
            .totalPhotographers(totalPhotographers)
            .totalCustomers(totalCustomers)
            .totalBookings(totalBookings)
            .totalRevenue(totalRevenue)
            .totalReviews(totalReviews)
            .averageRating(avgRating)
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
                .paymentStatus(b.getPaymentStatus() != null ? b.getPaymentStatus().name() : "UNPAID")
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
                .coverUrl(p.getUser().getCoverUrl())
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

    @Override
    @Transactional
    public void deleteBooking(Long bookingId) {
        // Delete associated payments first
        paymentRepository.deleteByBooking_Id(bookingId);
        
        // Delete associated reviews
        reviewRepository.deleteByBooking_Id(bookingId);
        
        // Then delete the booking
        bookingRepository.deleteById(bookingId);
    }

    @Override
    @Transactional
    public int migrateRevenueData() {
        List<com.example.fotori.model.Payment> payments = paymentRepository.findAll();
        int count = 0;
        for (com.example.fotori.model.Payment p : payments) {
            if (p.getStatus() == com.example.fotori.common.enums.PaymentStatus.PAID && p.getPlatformRevenue() == null) {
                p.setPlatformRevenue(calculatePlatformRevenue(p));
                paymentRepository.save(p);
                count++;
            }
        }
        return count;
    }

    private Double calculatePlatformRevenue(com.example.fotori.model.Payment payment) {
        if (payment.getSubscriptionPlan() != null) {
            // Subscription income is 100% platform revenue
            return payment.getAmount();
        }

        if (payment.getBooking() != null) {
            com.example.fotori.model.Booking booking = payment.getBooking();
            PhotographerProfile photographer = booking.getPhotographer();

            // Find active plan commission
            int commissionPercent = photographerSubscriptionRepository.findFirstByPhotographerAndActiveTrue(photographer)
                .map(s -> s.getPlan().getCommissionPercent())
                .orElse(10); // Default 10%

            double totalAmount = payment.getAmount() != null ? payment.getAmount() : 0.0;
            return 40000.0 + (totalAmount * commissionPercent / 100.0);
        }

        return 0.0;
    }
}
