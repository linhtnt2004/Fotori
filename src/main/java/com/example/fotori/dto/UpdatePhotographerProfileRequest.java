package com.example.fotori.dto;

import lombok.Getter;

@Getter
public class UpdatePhotographerProfileRequest {
    private String bio;
    private Integer experienceYears;
    private String avatarUrl;
}
