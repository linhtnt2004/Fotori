package com.example.fotori.repository;

import com.example.fotori.model.ForumCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumCategoryRepository extends JpaRepository<ForumCategory, Long> {
}
