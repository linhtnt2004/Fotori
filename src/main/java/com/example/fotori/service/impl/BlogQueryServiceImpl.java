package com.example.fotori.service.impl;

import com.example.fotori.dto.BlogPostResponse;
import com.example.fotori.model.BlogPost;
import com.example.fotori.repository.BlogRepository;
import com.example.fotori.service.BlogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogQueryServiceImpl implements BlogQueryService {

    private final BlogRepository blogRepository;

    @Override
    public Page<BlogPostResponse> getBlogs(
        int page,
        int size,
        String category,
        String tag,
        Boolean featured
    ) {

        PageRequest pageable = PageRequest.of(page, size);

        Page<BlogPost> blogs = blogRepository.findAll(pageable);

        return blogs.map(b ->
                             BlogPostResponse.builder()
                                 .id(b.getId())
                                 .title(b.getTitle())
                                 .slug(b.getSlug())
                                 .excerpt(b.getExcerpt())
                                 .coverImage(b.getCoverImage())
                                 .category(
                                     b.getCategory() != null
                                         ? b.getCategory().getName()
                                         : null
                                 )
                                 .tags(b.getTags())
                                 .featured(b.getFeatured())
                                 .likes(b.getLikes())
                                 .createdAt(b.getCreatedAt())
                                 .build()
        );
    }

    @Override
    public BlogPostResponse getBlogBySlug(String slug) {

        BlogPost blog = blogRepository.findBySlug(slug)
            .orElseThrow(() -> new RuntimeException("BLOG_NOT_FOUND"));

        return BlogPostResponse.builder()
            .id(blog.getId())
            .title(blog.getTitle())
            .slug(blog.getSlug())
            .excerpt(blog.getExcerpt())
            .coverImage(blog.getCoverImage())
            .category(
                blog.getCategory() != null
                    ? blog.getCategory().getName()
                    : null
            )
            .tags(blog.getTags())
            .featured(blog.getFeatured())
            .likes(blog.getLikes())
            .createdAt(blog.getCreatedAt())
            .build();
    }
}