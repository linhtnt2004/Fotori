package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.AddWishlistRequest;
import com.example.fotori.dto.WishlistItemResponse;
import com.example.fotori.service.WishlistCommandService;
import com.example.fotori.service.WishlistQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class WishlistController {

    private final WishlistQueryService wishlistQueryService;
    private final WishlistCommandService wishlistCommandService;

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

    @PostMapping
    public ResponseEntity<ApiResponse> addToWishlist(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody AddWishlistRequest request
    ) {

        WishlistItemResponse data =
            wishlistCommandService.addToWishlist(
                userDetails.getUsername(),
                request.getPhotographerId()
            );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Added to wishlist",
                data
            )
        );
    }

    @DeleteMapping("/{photographerId}")
    public ResponseEntity<ApiResponse> removeFromWishlist(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long photographerId
    ) {

        wishlistCommandService.removeFromWishlist(
            userDetails.getUsername(),
            photographerId
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Removed from wishlist",
                null
            )
        );
    }
}
