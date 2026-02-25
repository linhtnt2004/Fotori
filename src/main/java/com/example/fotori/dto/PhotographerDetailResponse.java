package com.example.fotori.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotographerDetailResponse {

    Long photographerId;
    String fullName;
    String avatarUrl;
    String bio;
    Integer experienceYears;
}
