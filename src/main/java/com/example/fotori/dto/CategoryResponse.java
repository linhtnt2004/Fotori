package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    Long id;

    String name;

    String description;
}