package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CustomerDashboardResponse {

    private CustomerDashboardStatsResponse stats;

    private List<BookingResponse> recentBookings;

    private List<PhotographerResponse> recommendedPhotographers;

}