package com.example.fotori.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateBlogRequest {

    String title;

    String content;

    String excerpt;

    String coverImage;

    String category;

    List<String> tags;

    Boolean featured;

}