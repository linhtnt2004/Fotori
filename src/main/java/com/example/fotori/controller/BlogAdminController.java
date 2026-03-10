package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.CreateBlogRequest;
import com.example.fotori.dto.UpdateBlogRequest;
import com.example.fotori.model.BlogPost;
import com.example.fotori.service.BlogAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BlogAdminController {

    private final BlogAdminService blogAdminService;

    @PostMapping
    public ResponseEntity<ApiResponse> createBlog(
        @RequestBody CreateBlogRequest request
    ) {

        BlogPost blog = blogAdminService.createBlog(request);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Blog created",
                blog
            )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBlog(
        @PathVariable Long id,
        @RequestBody UpdateBlogRequest request
    ) {

        BlogPost blog = blogAdminService.updateBlog(id, request);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Blog updated",
                blog
            )
        );
    }
}
