package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.service.PublicPhotographerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/photographers")
@RequiredArgsConstructor
public class PublicPhotographerController {

    private final PublicPhotographerService service;

    @GetMapping
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


}