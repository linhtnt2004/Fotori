package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicPhotographerItemResponse {

    Long id;
    String fullName;
    String avatarUrl;
    String bio;
    String city;
    String equipment;
    Integer experienceYears;
}
