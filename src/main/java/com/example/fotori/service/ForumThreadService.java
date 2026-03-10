package com.example.fotori.service;

import com.example.fotori.dto.CreateForumThreadRequest;
import com.example.fotori.model.ForumThread;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ForumThreadService {

    Page<ForumThread> getThreads(
        int page,
        int size,
        String category
    );

    Map<String, Object> getThreadDetail(Long id);

    ForumThread createThread(String email, CreateForumThreadRequest request);
}
