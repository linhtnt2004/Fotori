package com.example.fotori.service.impl;

import com.example.fotori.dto.CreateBlogRequest;
import com.example.fotori.dto.UpdateBlogRequest;
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

    @Override
    public BlogPost updateBlog(Long id, UpdateBlogRequest request) {

        BlogPost blog = blogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("BLOG_NOT_FOUND"));

        BlogCategory category = categoryRepository
            .findAll()
            .stream()
            .filter(c -> c.getName().equalsIgnoreCase(request.getCategory()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("CATEGORY_NOT_FOUND"));

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setExcerpt(request.getExcerpt());
        blog.setCoverImage(request.getCoverImage());
        blog.setCategory(category);
        blog.setTags(request.getTags());
        blog.setFeatured(request.getFeatured());

        blog.setSlug(generateSlug(request.getTitle()));

        return blogRepository.save(blog);
    }

    private String generateSlug(String title) {

        return title
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9\\s]", "")
            .replaceAll("\\s+", "-");
    }
}