package com.example.fotori.service.impl;

import com.example.fotori.model.Review;
import com.example.fotori.model.User;
import com.example.fotori.repository.ReviewRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.ReviewDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewDeleteServiceImpl implements ReviewDeleteService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public void deleteReview(String email, Long reviewId) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("REVIEW_NOT_FOUND"));

        boolean isAdmin = user.hasRole("ROLE_ADMIN");

        boolean isOwner =
            review.getCustomer().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("NO_PERMISSION");
        }

        reviewRepository.delete(review);
    }
}
