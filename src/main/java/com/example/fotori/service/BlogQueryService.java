package com.example.fotori.service;

import com.example.fotori.dto.BlogPostResponse;
import org.springframework.data.domain.Page;

public interface BlogQueryService {

    Page<BlogPostResponse> getBlogs(
        int page,
        int size,
        String category,
        String tag,
        Boolean featured
    );

    BlogPostResponse getBlogBySlug(String slug);
}