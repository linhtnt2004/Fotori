package com.example.fotori.repository;

import com.example.fotori.common.enums.ApprovalStatus;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotographerProfileRepository extends JpaRepository<PhotographerProfile, Long> {

    Optional<PhotographerProfile> findByUser(User user);

    List<PhotographerProfile> findByApprovalStatusAndDeletedAtIsNull(
        ApprovalStatus status
    );

    Optional<PhotographerProfile>
    findByIdAndApprovalStatusAndDeletedAtIsNull(
        Long id,
        ApprovalStatus status
    );
}
