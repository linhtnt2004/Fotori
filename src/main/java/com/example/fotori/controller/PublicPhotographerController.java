package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.PhotographerPublicDto;
import com.example.fotori.dto.PortfolioImageResponse;
import com.example.fotori.dto.PublicReviewResponse;
import com.example.fotori.service.PublicPhotographerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/photographers")
@RequiredArgsConstructor
public class PublicPhotographerController {

    private final PublicPhotographerService service;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Approved photographers",
                service.getAllApproved()
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDetail(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer detail",
                service.getDetail(id)
            )
        );
    }

    @GetMapping
    public ApiResponse getPhotographers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) Integer minPrice,
        @RequestParam(required = false) Integer maxPrice
    ) {

        Page<PhotographerPublicDto> result =
            service.getPhotographers(
                page,
                size,
                city,
                minPrice,
                maxPrice
            );

        Map<String, Object> data = Map.of(
            "result", result
        );

        return new ApiResponse(
            ErrorCode.SUCCESS.name(),
            "Photographers list",
            data
        );
    }

    @GetMapping("/{id}/reviews")
    public ApiResponse getPhotographerReviews(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {

        Page<PublicReviewResponse> result =
            service.getPhotographerReviews(id, page, size);

        Map<String, Object> data = Map.of(
            "data", result.getContent(),
            "total", result.getTotalElements()
        );

        return new ApiResponse(
            ErrorCode.SUCCESS.name(),
            "Photographer reviews",
            data
        );
    }

    @GetMapping("/{id}/packages")
    public ResponseEntity<ApiResponse> getPackages(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer packages",
                service.getPackages(id)
            )
        );
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<ApiResponse> getAvailability(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer availability",
                service.getAvailability(id)
            )
        );
    }

    @GetMapping("/{id}/portfolio")
    public ResponseEntity<ApiResponse> getPortfolio(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {

        Page<PortfolioImageResponse> result =
            service.getPortfolio(id, page, size);

        Map<String, Object> data = Map.of(
            "data", result.getContent()
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer portfolio",
                data
            )
        );
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse> getTrending(
        @RequestParam(defaultValue = "6") int limit
    ) {

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Trending photographers",
                service.getTrendingPhotographers(limit)
            )
        );
    }
}