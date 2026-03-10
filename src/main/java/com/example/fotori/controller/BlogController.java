package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.BlogPostResponse;
import com.example.fotori.service.BlogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogQueryService blogQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getBlogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String tag,
        @RequestParam(required = false) Boolean featured
    ) {

        Page<BlogPostResponse> blogs =
            blogQueryService.getBlogs(page, size, category, tag, featured);

        Map<String, Object> data = Map.of(
            "items", blogs.getContent(),
            "total", blogs.getTotalElements(),
            "page", blogs.getNumber(),
            "size", blogs.getSize(),
            "totalPages", blogs.getTotalPages()
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Blogs fetched successfully",
                data
            )
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse> getBlogDetail(
        @PathVariable String slug
    ) {

        BlogPostResponse blog =
            blogQueryService.getBlogBySlug(slug);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Blog detail",
                blog
            )
        );
    }
}