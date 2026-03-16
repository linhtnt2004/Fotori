package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AISuggestionResponse {

    private String title;

    private String description;

    private String action;

}