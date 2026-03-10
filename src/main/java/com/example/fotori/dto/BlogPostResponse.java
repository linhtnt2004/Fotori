package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BlogPostResponse {

    Long id;

    String title;

    String slug;

    String excerpt;

    String coverImage;

    String category;

    List<String> tags;

    Boolean featured;

    Integer likes;

    LocalDateTime createdAt;
}