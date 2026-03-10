package com.example.fotori.repository;

import com.example.fotori.model.Notification;
import com.example.fotori.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUser(User user, Pageable pageable);

    Page<Notification> findByUserAndIsReadFalse(User user, Pageable pageable);

    long countByUserAndIsReadFalse(User user);

}