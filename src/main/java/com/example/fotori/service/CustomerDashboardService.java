package com.example.fotori.service;

import com.example.fotori.dto.CustomerDashboardStatsResponse;
import com.example.fotori.dto.CustomerDashboardResponse;

public interface CustomerDashboardService {

    CustomerDashboardStatsResponse getCustomerStats(String email);

    CustomerDashboardResponse getCustomerDashboard(String email);
}
