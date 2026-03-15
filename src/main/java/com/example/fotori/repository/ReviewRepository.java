package com.example.fotori.repository;

import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByPhotographerId(
        Long photographerId,
        Pageable pageable
    );

    @Query("""
        SELECT r.photographer.id,
               AVG(r.rating),
               COUNT(r.id)
        FROM Review r
        GROUP BY r.photographer.id
        ORDER BY AVG(r.rating) DESC, COUNT(r.id) DESC
    """)
    List<Object[]> findTrendingPhotographers(Pageable pageable);

    boolean existsByBooking(Booking booking);

    @Query("""
        SELECT AVG(r.rating)
        FROM Review r
        WHERE r.photographer.id = :photographerId
    """)
    Double getAverageRating(Long photographerId);

    Optional<Review> findByIdAndPhotographerId(
        Long reviewId,
        Long photographerId
    );

    long countByPhotographer(PhotographerProfile photographer);

    @Query("""
        SELECT COALESCE(AVG(r.rating),0)
        FROM Review r
        WHERE r.photographer = :photographer
    """)
    Double getAverageRating(PhotographerProfile photographer);

    List<Review> findByPhotographerOrderByCreatedAtDesc(
        PhotographerProfile photographer,
        Pageable pageable
    );

    void deleteByBooking_Id(Long bookingId);
}