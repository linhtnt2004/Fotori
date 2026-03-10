package com.example.fotori.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateForumThreadRequest {

    String title;

    String content;

    String category;

    List<String> tags;

}