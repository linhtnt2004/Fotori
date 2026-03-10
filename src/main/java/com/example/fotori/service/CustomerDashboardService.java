package com.example.fotori.service;

import com.example.fotori.dto.CustomerDashboardStatsResponse;

public interface CustomerDashboardService {

    CustomerDashboardStatsResponse getCustomerStats(String email);
}
