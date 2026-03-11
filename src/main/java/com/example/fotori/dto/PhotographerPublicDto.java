package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotographerPublicDto {

    Long id;

    String name;

    String email;

    String avatar;

    String city;

    String bio;

    Integer experienceYears;

    Integer startingPrice;

}