package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistItemResponse {

    Long photographerId;

    String photographerName;

    String avatarUrl;

    Double averageRating;

}