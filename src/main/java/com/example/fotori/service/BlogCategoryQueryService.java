package com.example.fotori.service;

import com.example.fotori.dto.BlogCategoryResponse;
import java.util.List;

public interface BlogCategoryQueryService {

    List<BlogCategoryResponse> getCategories();

}