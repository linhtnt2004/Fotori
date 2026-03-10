package com.example.fotori.service;

import com.example.fotori.dto.WishlistItemResponse;

import java.util.List;

public interface WishlistQueryService {

    List<WishlistItemResponse> getWishlist(String email);

}
