package com.example.fotori.repository;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotographerProfileRepository extends JpaRepository<PhotographerProfile, Long> {

    Optional<PhotographerProfile> findByUser(User user);

    Optional<PhotographerProfile> findByUserId(Long userId);

    List<PhotographerProfile> findByApprovalStatusAndDeletedAtIsNull(
        ApprovalStatus status
    );

    Optional<PhotographerProfile>
    findByIdAndApprovalStatusAndDeletedAtIsNull(
        Long id,
        ApprovalStatus status
    );

    Page<PhotographerProfile> findByApprovalStatus(
        ApprovalStatus status,
        Pageable pageable
    );

    @Query("""
        SELECT p
        FROM PhotographerProfile p
        JOIN p.user u
        WHERE p.approvalStatus = 'APPROVED'
        AND p.deletedAt IS NULL
        AND (:keyword IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')))
        """)
    List<PhotographerProfile> searchPhotographers(
        String keyword,
        String city
    );
}
