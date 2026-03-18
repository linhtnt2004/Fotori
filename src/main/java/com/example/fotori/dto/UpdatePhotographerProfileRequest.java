package com.example.fotori.dto;

import lombok.Getter;

@Getter
public class UpdatePhotographerProfileRequest {
    private String bio;
    private String city;
    private String equipment;
    private Integer experienceYears;
    private String avatarUrl;
}
