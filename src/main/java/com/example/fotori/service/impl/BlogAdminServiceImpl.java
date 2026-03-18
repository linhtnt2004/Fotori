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

        String categoryName = request.getCategory() == null || request.getCategory().trim().isEmpty() 
            ? "Chung" : request.getCategory().trim();

        BlogCategory category = categoryRepository.findByNameIgnoreCase(categoryName)
            .orElseGet(() -> {
                BlogCategory newCat = BlogCategory.builder()
                    .name(categoryName)
                    .slug(generateSlug(categoryName))
                    .build();
                return categoryRepository.save(newCat);
            });

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

        String categoryName = request.getCategory() == null || request.getCategory().trim().isEmpty() 
            ? "Chung" : request.getCategory().trim();

        BlogCategory category = categoryRepository.findByNameIgnoreCase(categoryName)
            .orElseGet(() -> {
                BlogCategory newCat = BlogCategory.builder()
                    .name(categoryName)
                    .slug(generateSlug(categoryName))
                    .build();
                return categoryRepository.save(newCat);
            });

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

    @Override
    public void deleteBlog(Long id) {

        BlogPost blog = blogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("BLOG_NOT_FOUND"));

        blogRepository.delete(blog);
    }

    @Override
    public int likeBlog(Long id) {

        BlogPost blog = blogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("BLOG_NOT_FOUND"));

        int likes = blog.getLikes() == null ? 0 : blog.getLikes();

        blog.setLikes(likes + 1);

        blogRepository.save(blog);

        return blog.getLikes();
    }

    private String generateSlug(String title) {

        return title
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9\\s]", "")
            .replaceAll("\\s+", "-");
    }
}