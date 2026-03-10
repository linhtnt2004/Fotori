package com.example.fotori.service.impl;

import com.example.fotori.dto.BlogCategoryResponse;
import com.example.fotori.repository.BlogCategoryRepository;
import com.example.fotori.service.BlogCategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogCategoryQueryServiceImpl implements BlogCategoryQueryService {

    private final BlogCategoryRepository blogCategoryRepository;

    @Override
    public List<BlogCategoryResponse> getCategories() {

        return blogCategoryRepository.findAll()
            .stream()
            .map(c ->
                     BlogCategoryResponse.builder()
                         .id(c.getId())
                         .name(c.getName())
                         .slug(c.getSlug())
                         .build()
            )
            .toList();
    }
}