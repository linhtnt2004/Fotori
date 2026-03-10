package com.example.fotori.service.impl;

import com.example.fotori.dto.WishlistItemResponse;
import com.example.fotori.model.User;
import com.example.fotori.repository.ReviewRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.repository.WishlistRepository;
import com.example.fotori.service.WishlistQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistQueryServiceImpl implements WishlistQueryService {

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<WishlistItemResponse> getWishlist(String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        return wishlistRepository.findByUser(user)
            .stream()
            .map(w -> {

                Double avgRating =
                    reviewRepository.getAverageRating(
                        w.getPhotographer().getId()
                    );

                return WishlistItemResponse.builder()
                    .photographerId(w.getPhotographer().getId())
                    .photographerName(
                        w.getPhotographer().getUser().getFullName()
                    )
                    .avatarUrl(
                        w.getPhotographer().getUser().getAvatarUrl()
                    )
                    .averageRating(avgRating)
                    .build();
            })
            .toList();
    }
}
