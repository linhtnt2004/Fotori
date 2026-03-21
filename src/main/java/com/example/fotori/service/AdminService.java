package com.example.fotori.service;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.dto.admin.*;

import java.util.List;

public interface AdminService {
    AdminStatsDTO getDashboardStats();
    List<AdminUserDTO> getAllUsers();
    List<AdminBookingDTO> getAllBookings();
    List<AdminPhotographerDTO> getAllPhotographers();
    void updatePhotographerStatus(Long photographerId, ApprovalStatus status);
    void deleteBooking(Long bookingId);
    int migrateRevenueData();
}
