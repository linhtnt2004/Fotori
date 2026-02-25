package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicPhotographerDetailResponse {

    Long id;
    String fullName;
    String avatarUrl;
    String bio;
    Integer experienceYears;
}
