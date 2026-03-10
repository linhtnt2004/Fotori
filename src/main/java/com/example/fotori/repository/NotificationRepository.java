package com.example.fotori.repository;

import com.example.fotori.model.Notification;
import com.example.fotori.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUser(User user, Pageable pageable);

    Page<Notification> findByUserAndIsReadFalse(User user, Pageable pageable);

    long countByUserAndIsReadFalse(User user);


    @Modifying
    @Transactional
    @Query("""
        UPDATE Notification n
        SET n.isRead = true
        WHERE n.user = :user AND n.isRead = false
        """)
    void markAllAsRead(User user);
}