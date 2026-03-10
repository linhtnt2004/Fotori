package com.example.fotori.service.impl;

import com.example.fotori.model.ForumThread;
import com.example.fotori.repository.ForumThreadRepository;
import com.example.fotori.service.ForumThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForumThreadServiceImpl implements ForumThreadService {

    private final ForumThreadRepository forumThreadRepository;

    @Override
    public Page<ForumThread> getThreads(
        int page,
        int size,
        String category
    ) {

        Pageable pageable =
            PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (category != null) {
            return forumThreadRepository.findByCategory_Slug(
                category,
                pageable
            );
        }

        return forumThreadRepository.findAll(pageable);
    }
}
