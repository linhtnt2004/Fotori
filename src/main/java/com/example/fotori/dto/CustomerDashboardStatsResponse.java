package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDashboardStatsResponse {

    Long totalBookings;

    Double totalSpent;

    Long upcomingBookings;

}