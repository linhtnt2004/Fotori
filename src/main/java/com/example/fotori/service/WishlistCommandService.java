package com.example.fotori.service;

import com.example.fotori.dto.WishlistItemResponse;

public interface WishlistCommandService {

    WishlistItemResponse addToWishlist(String email, Long photographerId);

}