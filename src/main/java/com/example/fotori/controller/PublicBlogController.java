package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.service.BlogAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public/blog")
@RequiredArgsConstructor
public class PublicBlogController {

    private final BlogAdminService blogAdminService;

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse> likeBlog(
        @PathVariable Long id
    ) {

        int likes = blogAdminService.likeBlog(id);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Blog liked",
                Map.of("likes", likes)
            )
        );
    }
}
