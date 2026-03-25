package com.example.fotori.service;

import com.example.fotori.dto.DashboardRecentBookingResponse;
import com.example.fotori.dto.DashboardRecentReviewResponse;
import com.example.fotori.dto.DashboardTransactionResponse;
import com.example.fotori.dto.PhotographerDashboardStatsResponse;

import java.util.List;

public interface PhotographerDashboardService {

    PhotographerDashboardStatsResponse getStats(String email);

    List<DashboardRecentBookingResponse> getRecentBookings(String email);

    List<DashboardRecentReviewResponse> getRecentReviews(String email);

    List<DashboardTransactionResponse> getRecentTransactions(String email);
}
