package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.MatchingRequestDto;
import com.example.fotori.dto.PhotographerMatchDto;
import com.example.fotori.service.AIMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/matching")
@RequiredArgsConstructor
public class AIMatchingController {

    private final AIMatchingService aiMatchingService;

    @PostMapping
    public ResponseEntity<ApiResponse> findMatches(
            @RequestBody MatchingRequestDto request) {
        List<PhotographerMatchDto> results =
            aiMatchingService.findMatchingPhotographers(request);
        return ResponseEntity.ok(
            new ApiResponse("200", "Success", results)
        );
    }
}