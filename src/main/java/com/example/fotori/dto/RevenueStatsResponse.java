package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevenueStatsResponse {

    String period;

    Double revenue;

    Long totalBookings;

}