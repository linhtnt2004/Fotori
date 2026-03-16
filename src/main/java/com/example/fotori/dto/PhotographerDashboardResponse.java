package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PhotographerDashboardResponse {

    private PhotographerDashboardStatsResponse stats;

    private List<DashboardRecentBookingResponse> upcomingBookings;

    private List<AISuggestionResponse> aiSuggestions;

}