package com.example.fotori.service.impl;

import com.example.fotori.dto.WishlistItemResponse;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import com.example.fotori.model.Wishlist;
import com.example.fotori.repository.PhotographerProfileRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.repository.WishlistRepository;
import com.example.fotori.service.WishlistCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistCommandServiceImpl implements WishlistCommandService {

    private final UserRepository userRepository;
    private final PhotographerProfileRepository photographerRepository;
    private final WishlistRepository wishlistRepository;

    @Override
    public WishlistItemResponse addToWishlist(String email, Long photographerId) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        PhotographerProfile photographer =
            photographerRepository.findById(photographerId)
                .orElseThrow(() -> new RuntimeException("PHOTOGRAPHER_NOT_FOUND"));

        boolean exists = wishlistRepository
            .existsByUserAndPhotographer(user, photographer);

        if (exists) {
            throw new RuntimeException("ALREADY_IN_WISHLIST");
        }

        Wishlist wishlist = Wishlist.builder()
            .user(user)
            .photographer(photographer)
            .build();

        wishlistRepository.save(wishlist);

        return WishlistItemResponse.builder()
            .photographerId(photographer.getId())
            .photographerName(
                photographer.getUser().getFullName()
            )
            .avatarUrl(
                photographer.getUser().getAvatarUrl()
            )
            .build();
    }
}
