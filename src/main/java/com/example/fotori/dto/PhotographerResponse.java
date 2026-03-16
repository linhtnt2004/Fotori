package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotographerResponse {

    private Long id;

    private String name;

    private String specialty;

    private double rating;

    private int reviews;

    private String price;

    private String image;

    private String badge;

}