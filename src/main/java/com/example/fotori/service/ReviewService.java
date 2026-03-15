package com.example.fotori.service;

import com.example.fotori.dto.CreateReviewRequest;
import com.example.fotori.dto.ReviewResponse;

public interface ReviewService {

    ReviewResponse createReview(String email, CreateReviewRequest request);
}
