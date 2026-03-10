package com.example.fotori.service;

import com.example.fotori.model.ForumThread;
import org.springframework.data.domain.Page;

public interface ForumThreadService {

    Page<ForumThread> getThreads(
        int page,
        int size,
        String category
    );
}
