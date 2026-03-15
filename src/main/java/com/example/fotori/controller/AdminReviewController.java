package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.ReviewResponse;
import com.example.fotori.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllReviews() {
        List<ReviewResponse> reviews = reviewQueryService.getAllReviews();
        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Total reviews fetched",
                reviews
            )
        );
    }
}
