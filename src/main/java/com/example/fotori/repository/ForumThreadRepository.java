package com.example.fotori.repository;

import com.example.fotori.model.ForumThread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {

    Page<ForumThread> findByCategory_Slug(
        String slug,
        Pageable pageable
    );
}
