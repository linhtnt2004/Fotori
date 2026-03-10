package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePortfolioRequest {

    String imageUrl;

    String title;

    String category;

    String description;

}