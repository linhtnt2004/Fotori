package com.example.fotori.repository;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.model.Booking;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPhotographer(PhotographerProfile photographer);

    List<Booking> findByPhotographerAndStatus(
        PhotographerProfile photographer,
        BookingStatus status
    );

    Optional<Booking> findByIdAndPhotographer(
        Long id,
        PhotographerProfile photographer
    );

    List<Booking> findByUser(User user);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.photographer = :photographer
              AND b.status = 'ACCEPTED'
              AND (
                :start < b.endTime AND :end > b.startTime
              )
        """)
    List<Booking> findAcceptedOverlappingBookings(
        @Param("photographer") PhotographerProfile photographer,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.photographer = :photographer
              AND b.status = 'ACCEPTED'
              AND (:start < b.endTime AND :end > b.startTime)
        """)
    boolean existsAcceptedOverlapping(
        @Param("photographer") PhotographerProfile photographer,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    Optional<Booking> findByIdAndUser(Long id, User user);

    Page<Booking> findByUser(
        User user,
        Pageable pageable
    );

    Page<Booking> findByUserAndStatus(
        User user,
        BookingStatus status,
        Pageable pageable
    );

    Page<Booking> findByPhotographer(
        PhotographerProfile photographer,
        Pageable pageable
    );

    Page<Booking> findByPhotographerAndStatus(
        PhotographerProfile photographer,
        BookingStatus status,
        Pageable pageable
    );

    long countByPhotographer(PhotographerProfile photographer);

    long countByPhotographerAndPhotographerStatus(
        PhotographerProfile photographer,
        BookingActorStatus status
    );

    @Query("""
        SELECT COALESCE(SUM(b.finalPrice),0)
        FROM Booking b
        WHERE b.photographer = :photographer
        AND b.paymentStatus = 'PAID'
        """)
    Double getTotalRevenue(PhotographerProfile photographer);

    List<Booking> findByPhotographerOrderByCreatedAtDesc(
        PhotographerProfile photographer,
        Pageable pageable
    );
}
