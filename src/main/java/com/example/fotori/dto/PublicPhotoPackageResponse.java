package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicPhotoPackageResponse {

    Long packageId;

    String title;

    String description;

    Integer price;

    Integer durationMinutes;

}