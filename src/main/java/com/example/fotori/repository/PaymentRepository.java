package com.example.fotori.repository;

import com.example.fotori.common.enums.PaymentStatus;
import com.example.fotori.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByBooking_User_Id(
        Long userId,
        Pageable pageable
    );

    java.util.List<Payment> findAllByBookingIsNotNullOrderByCreatedAtDesc();

    java.util.List<Payment> findAllBySubscriptionPlanIsNotNullOrderByCreatedAtDesc();

    java.util.List<Payment> findAllBySubscriptionPlanIsNotNull();

    boolean existsByBooking_IdAndStatus(
        Long bookingId,
        PaymentStatus status
    );

    void deleteByBooking_Id(Long bookingId);

    Page<Payment> findByPhotographer_Id(
        Long photographerId,
        Pageable pageable
    );

    @Query("""
            SELECT FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m'),
                   SUM(p.platformRevenue),
                   COUNT(p)
            FROM Payment p
            WHERE p.status = 'PAID'
            AND p.createdAt BETWEEN :startDate AND :endDate
            GROUP BY FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m')
            ORDER BY 1
        """)
    java.util.List<Object[]> getRevenueByMonth(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m-%d'),
               SUM(p.platformRevenue),
               COUNT(p)
        FROM Payment p
        WHERE p.status = 'PAID'
        AND p.createdAt BETWEEN :startDate AND :endDate
        GROUP BY FUNCTION('DATE_FORMAT', p.createdAt, '%Y-%m-%d')
        ORDER BY 1
        """)
    java.util.List<Object[]> getRevenueByDay(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT FUNCTION('DATE_FORMAT', p.createdAt, '%Y'),
               SUM(p.platformRevenue),
               COUNT(p)
        FROM Payment p
        WHERE p.status = 'PAID'
        AND p.createdAt BETWEEN :startDate AND :endDate
        GROUP BY FUNCTION('DATE_FORMAT', p.createdAt, '%Y')
        ORDER BY 1
        """)
    java.util.List<Object[]> getRevenueByYear(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}