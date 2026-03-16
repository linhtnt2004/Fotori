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

    Page<Booking> findByUserAndPaymentStatus(
        User user,
        com.example.fotori.common.enums.PaymentStatus paymentStatus,
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

    Page<Booking> findByPhotographerAndPaymentStatus(
        PhotographerProfile photographer,
        com.example.fotori.common.enums.PaymentStatus paymentStatus,
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

    long countByUser(User user);

    @Query("""
        SELECT COALESCE(SUM(b.finalPrice),0)
        FROM Booking b
        WHERE b.user = :user
        AND b.paymentStatus = 'PAID'
        """)
    Double getTotalSpent(User user);

    @Query("""
        SELECT COUNT(b)
        FROM Booking b
        WHERE b.user = :user
        AND b.startTime > CURRENT_TIMESTAMP
        AND b.status = 'CONFIRMED'
        """)
    Long countUpcomingBookings(User user);

    @Query(value = """
        SELECT DATE_FORMAT(b.created_at, '%Y-%m') as month,
               SUM(b.final_price) as revenue,
               COUNT(b.id) as count
        FROM booking b
        WHERE b.payment_status = 'PAID'
        AND b.created_at BETWEEN :startDate AND :endDate
        GROUP BY DATE_FORMAT(b.created_at, '%Y-%m')
        ORDER BY DATE_FORMAT(b.created_at, '%Y-%m')
        """, nativeQuery = true)
    List<Object[]> getRevenueByMonth(
        LocalDateTime startDate,
        LocalDateTime endDate
    );

    @Query(value = """
        SELECT DATE_FORMAT(b.created_at, '%Y-%m-%d') as day,
               SUM(b.final_price) as revenue,
               COUNT(b.id) as count
        FROM booking b
        WHERE b.payment_status = 'PAID'
        AND b.created_at BETWEEN :startDate AND :endDate
        GROUP BY DATE_FORMAT(b.created_at, '%Y-%m-%d')
        ORDER BY day
        """, nativeQuery = true)
    List<Object[]> getRevenueByDay(
        LocalDateTime startDate,
        LocalDateTime endDate
    );

    @Query(value = """
        SELECT DATE_FORMAT(b.created_at, '%Y') as year,
               SUM(b.final_price) as revenue,
               COUNT(b.id) as count
        FROM booking b
        WHERE b.payment_status = 'PAID'
        AND b.created_at BETWEEN :startDate AND :endDate
        GROUP BY DATE_FORMAT(b.created_at, '%Y')
        ORDER BY year
        """, nativeQuery = true)
    List<Object[]> getRevenueByYear(
        LocalDateTime startDate,
        LocalDateTime endDate
    );
}
