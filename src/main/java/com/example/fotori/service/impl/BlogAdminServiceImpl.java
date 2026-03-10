package com.example.fotori.service.impl;

import com.example.fotori.dto.CreateBlogRequest;
import com.example.fotori.model.BlogCategory;
import com.example.fotori.model.BlogPost;
import com.example.fotori.repository.BlogCategoryRepository;
import com.example.fotori.repository.BlogRepository;
import com.example.fotori.service.BlogAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BlogAdminServiceImpl implements BlogAdminService {

    private final BlogRepository blogRepository;
    private final BlogCategoryRepository categoryRepository;

    @Override
    public BlogPost createBlog(CreateBlogRequest request) {

        BlogCategory category = categoryRepository
            .findAll()
            .stream()
            .filter(c -> c.getName().equalsIgnoreCase(request.getCategory()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("CATEGORY_NOT_FOUND"));

        BlogPost blog = BlogPost.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .excerpt(request.getExcerpt())
            .coverImage(request.getCoverImage())
            .category(category)
            .tags(request.getTags())
            .slug(generateSlug(request.getTitle()))
            .featured(false)
            .likes(0)
            .build();

        return blogRepository.save(blog);
    }

    private String generateSlug(String title) {

        return title
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9\\s]", "")
            .replaceAll("\\s+", "-");
    }
}