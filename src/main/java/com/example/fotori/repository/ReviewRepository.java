package com.example.fotori.repository;

import com.example.fotori.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByPhotographerProfileId(
        Long photographerId,
        Pageable pageable
    );

}