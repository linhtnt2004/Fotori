package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReviewRequest {

    Long photographerId;

    Long bookingId;

    Integer rating;

    Integer skills;

    Integer attitude;

    Integer punctuality;

    Integer postProcessing;

    String comment;

    List<String> images;
}
