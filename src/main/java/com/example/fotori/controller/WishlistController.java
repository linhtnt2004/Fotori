package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.WishlistItemResponse;
import com.example.fotori.service.WishlistQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class WishlistController {

    private final WishlistQueryService wishlistQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getWishlist(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        List<WishlistItemResponse> wishlist =
            wishlistQueryService.getWishlist(
                userDetails.getUsername()
            );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Wishlist",
                wishlist
            )
        );
    }
}
