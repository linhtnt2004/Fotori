package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAvailabilityRequest {
    LocalDateTime startTime;
    LocalDateTime endTime;
}
