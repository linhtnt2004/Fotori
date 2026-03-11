package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.RevenueGroupBy;
import com.example.fotori.dto.RevenueStatsResponse;
import com.example.fotori.service.AdminRevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRevenueController {

    private final AdminRevenueService revenueService;

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse> getRevenue(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate,
        @RequestParam(defaultValue = "MONTH") RevenueGroupBy groupBy
    ) {

        List<RevenueStatsResponse> data =
            revenueService.getRevenue(startDate, endDate, groupBy);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Revenue statistics fetched successfully",
                data
            )
        );
    }
}
