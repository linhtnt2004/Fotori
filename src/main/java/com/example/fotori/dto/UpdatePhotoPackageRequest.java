package com.example.fotori.dto;

import lombok.Data;

@Data
public class UpdatePhotoPackageRequest {

    private String title;

    private String description;

    private Integer durationMinutes;

    private Integer price;

}