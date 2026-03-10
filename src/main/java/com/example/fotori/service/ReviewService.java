package com.example.fotori.service;

import com.example.fotori.dto.CreateReviewRequest;
import com.example.fotori.model.Review;

public interface ReviewService {

    Review createReview(String email, CreateReviewRequest request);
}
