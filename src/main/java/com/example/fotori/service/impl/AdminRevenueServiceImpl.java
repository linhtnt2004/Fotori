package com.example.fotori.service.impl;

import com.example.fotori.common.enums.RevenueGroupBy;
import com.example.fotori.dto.RevenueStatsResponse;
import com.example.fotori.service.AdminRevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRevenueServiceImpl implements AdminRevenueService {

    private final com.example.fotori.repository.PaymentRepository paymentRepository;

    @Override
    public List<RevenueStatsResponse> getRevenue(
        LocalDate startDate,
        LocalDate endDate,
        RevenueGroupBy groupBy
    ) {

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23,59,59);

        List<Object[]> results;

        switch (groupBy) {

            case DAY:
                results = paymentRepository.getRevenueByDay(start, end);
                break;

            case YEAR:
                results = paymentRepository.getRevenueByYear(start, end);
                break;

            default:
                results = paymentRepository.getRevenueByMonth(start, end);
        }

        return results.stream()
            .map(row -> RevenueStatsResponse.builder()
                .period((String) row[0])
                .revenue((Double) row[1])
                .totalBookings((Long) row[2])
                .build())
            .toList();
    }
}
