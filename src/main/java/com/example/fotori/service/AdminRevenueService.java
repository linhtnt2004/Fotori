package com.example.fotori.service;

import com.example.fotori.common.enums.RevenueGroupBy;
import com.example.fotori.dto.RevenueStatsResponse;

import java.time.LocalDate;
import java.util.List;

public interface AdminRevenueService {

    List<RevenueStatsResponse> getRevenue(
        LocalDate startDate,
        LocalDate endDate,
        RevenueGroupBy groupBy
    );
}
