package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicAvailabilityResponse {

    Long availabilityId;
    LocalDateTime startTime;
    LocalDateTime endTime;
}