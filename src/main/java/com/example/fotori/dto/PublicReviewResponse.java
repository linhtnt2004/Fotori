package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicReviewResponse {

    Long id;

    String customerName;

    String customerAvatar;

    Integer rating;

    String comment;

    LocalDateTime createdAt;
}