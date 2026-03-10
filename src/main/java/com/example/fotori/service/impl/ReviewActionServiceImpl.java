package com.example.fotori.service.impl;

import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Review;
import com.example.fotori.model.User;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.repository.ReviewRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.ReviewActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewActionServiceImpl implements ReviewActionService {

    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Review respondReview(
        String photographerEmail,
        Long reviewId,
        String response
    ) {

        User user = userRepository.findByEmail(photographerEmail)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        PhotographerProfile photographer =
            photographerRepository.findByUser(user)
                .orElseThrow(() ->
                                 new RuntimeException("PHOTOGRAPHER_PROFILE_NOT_FOUND")
                );

        Review review = reviewRepository
            .findByIdAndPhotographerProfile_Id(
                reviewId,
                photographer.getId()
            )
            .orElseThrow(() ->
                             new RuntimeException("REVIEW_NOT_FOUND")
            );

        if (review.getResponse() != null) {
            throw new RuntimeException("REVIEW_ALREADY_RESPONDED");
        }

        review.setResponse(response);

        return reviewRepository.save(review);
    }
}
