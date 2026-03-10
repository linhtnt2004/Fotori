package com.example.fotori.service;

public interface ReviewDeleteService {

    void deleteReview(
        String email,
        Long reviewId
    );

}
