package com.example.fotori.repository;

import com.example.fotori.model.Booking;
import com.example.fotori.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByPhotographerProfileId(
        Long photographerId,
        Pageable pageable
    );

    @Query("""
        SELECT r.photographerProfile.id,
               AVG(r.rating),
               COUNT(r.id)
        FROM Review r
        GROUP BY r.photographerProfile.id
        ORDER BY AVG(r.rating) DESC, COUNT(r.id) DESC
        """)
    List<Object[]> findTrendingPhotographers(Pageable pageable);

    boolean existsByBooking(Booking booking);
}