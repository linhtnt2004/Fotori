package com.example.fotori.service.impl;

import com.example.fotori.dto.ReviewResponse;
import com.example.fotori.model.Review;
import com.example.fotori.repository.ReviewRepository;
import com.example.fotori.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewRepository reviewRepository;

    @Override
    public Map<String, Object> getPhotographerReviews(
        Long photographerId,
        int page,
        int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviewPage =
            reviewRepository.findByPhotographerId(
                photographerId,
                pageable
            );
        
        Double avg = reviewRepository.getAverageRating(photographerId);

        List<ReviewResponse> reviews = reviewPage
            .getContent()
            .stream()
            .map(this::mapToResponse)
            .toList();

        return Map.of(
            "data", reviews,
            "averageRating", avg == null ? 0 : avg
        );
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll(
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "createdAt")
        ).stream()
        .map(this::mapToResponse)
        .toList();
    }

    private ReviewResponse mapToResponse(Review r) {
        return ReviewResponse.builder()
            .id(r.getId())
            .customerName(r.getCustomer().getFullName())
            .photographerName(r.getPhotographer().getUser().getFullName())
            .rating(r.getRating())
            .skills(r.getSkills())
            .attitude(r.getAttitude())
            .punctuality(r.getPunctuality())
            .postProcessing(r.getPostProcessing())
            .comment(r.getComment())
            .photographerResponse(r.getResponse())
            .createdAt(r.getCreatedAt())
            .build();
    }
}
