package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotographerDashboardStatsResponse {

    Long totalBookings;

    Double totalRevenue;

    Double averageRating;

    Long totalReviews;

    Long pendingBookings;

}