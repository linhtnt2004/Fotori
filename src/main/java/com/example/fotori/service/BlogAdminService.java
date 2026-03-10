package com.example.fotori.service;

import com.example.fotori.dto.CreateBlogRequest;
import com.example.fotori.dto.UpdateBlogRequest;
import com.example.fotori.model.BlogPost;

public interface BlogAdminService {

    BlogPost createBlog(CreateBlogRequest request);

    BlogPost updateBlog(Long id, UpdateBlogRequest request);
}