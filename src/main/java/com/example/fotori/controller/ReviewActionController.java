package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.ReviewResponseRequest;
import com.example.fotori.model.Review;
import com.example.fotori.service.ReviewActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGRAPHER')")
public class ReviewActionController {

    private final ReviewActionService reviewActionService;

    @PostMapping("/{id}/response")
    public ResponseEntity<ApiResponse> respondReview(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id,
        @RequestBody ReviewResponseRequest request
    ) {

        Review review =
            reviewActionService.respondReview(
                userDetails.getUsername(),
                id,
                request.getResponse()
            );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Review responded",
                review
            )
        );
    }
}
