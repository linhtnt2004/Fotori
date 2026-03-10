package com.example.fotori.service;

import com.example.fotori.model.Review;

public interface ReviewActionService {

    Review respondReview(
        String photographerEmail,
        Long reviewId,
        String response
    );

}
