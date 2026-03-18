package com.example.fotori.repository;

import com.example.fotori.model.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {
    Optional<BlogCategory> findByNameIgnoreCase(String name);
}