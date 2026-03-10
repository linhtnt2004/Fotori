package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBlogRequest {

    String title;

    String content;

    String excerpt;

    String coverImage;

    String category;

    List<String> tags;

}