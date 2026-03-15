package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {

    Long id;

    String customerName;

    String photographerName;

    Integer rating;

    Integer skills;

    Integer attitude;

    Integer punctuality;

    Integer postProcessing;

    String comment;

    String photographerResponse;

    LocalDateTime createdAt;
}