package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.CreatePortfolioRequest;
import com.example.fotori.dto.PortfolioResponse;
import com.example.fotori.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Photographer Portfolio", description = "Manage photographer portfolio")
@RestController
@RequestMapping("/api/photographer/portfolio")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHOTOGPAHER')")
public class PhotographerPortfolioController {

    private final PortfolioService portfolioService;

    @Operation(summary = "Get current photographer portfolio")
    @GetMapping
    public ResponseEntity<ApiResponse> getMyPortfolio(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        List<PortfolioResponse> portfolio =
            portfolioService.getMyPortfolio(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Portfolio fetched successfully",
                portfolio
            )
        );
    }

    @Operation(summary = "Upload portfolio image")
    @PostMapping
    public ResponseEntity<ApiResponse> createPortfolio(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody CreatePortfolioRequest request
    ) {

        PortfolioResponse portfolio =
            portfolioService.createPortfolio(
                userDetails.getUsername(),
                request
            );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Portfolio image created successfully",
                portfolio
            )
        );
    }
}