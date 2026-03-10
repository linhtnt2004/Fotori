package com.example.fotori.repository;

import com.example.fotori.model.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<BlogPost, Long> {

    Page<BlogPost> findByCategory_Slug(String slug, Pageable pageable);

    Optional<BlogPost> findBySlug(String slug);
}