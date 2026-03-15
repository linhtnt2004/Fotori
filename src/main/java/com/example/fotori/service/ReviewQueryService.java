package com.example.fotori.service;

import java.util.Map;

public interface ReviewQueryService {

    Map<String, Object> getPhotographerReviews(
        Long photographerId,
        int page,
        int size
    );

    java.util.List<com.example.fotori.dto.ReviewResponse> getAllReviews();
}
