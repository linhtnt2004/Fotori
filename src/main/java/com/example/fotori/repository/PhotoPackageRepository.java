package com.example.fotori.repository;

import com.example.fotori.model.PhotoPackage;
import com.example.fotori.model.PhotographerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhotoPackageRepository extends JpaRepository<PhotoPackage, Long> {

    // ── Giữ nguyên các method cũ ──────────────────────────────
    List<PhotoPackage> findByPhotographerProfileAndActiveTrue(PhotographerProfile photographerProfile);
    List<PhotoPackage> findByActiveTrue();
    List<PhotoPackage> findByPhotographerProfileIdAndActiveTrue(Long photographerId);

    // ── Thêm mới cho AI Matching ───────────────────────────────

    // Lấy giá thấp nhất trong packages active của photographer
    @Query("SELECT MIN(p.price) FROM PhotoPackage p " +
           "WHERE p.photographerProfile.id = :photographerId AND p.active = true")
    Integer findMinPriceByPhotographerId(@Param("photographerId") Long photographerId);

    // Lấy giá cao nhất trong packages active của photographer
    @Query("SELECT MAX(p.price) FROM PhotoPackage p " +
           "WHERE p.photographerProfile.id = :photographerId AND p.active = true")
    Integer findMaxPriceByPhotographerId(@Param("photographerId") Long photographerId);

    // Lấy danh sách concept names của photographer (qua photo_package_concepts)
    @Query("SELECT DISTINCT c.name FROM PhotoPackage p " +
           "JOIN p.concepts c " +
           "WHERE p.photographerProfile.id = :photographerId AND p.active = true")
    List<String> findConceptNamesByPhotographerId(@Param("photographerId") Long photographerId);

    // Lấy danh sách concept IDs của photographer
    @Query("SELECT DISTINCT c.id FROM PhotoPackage p " +
           "JOIN p.concepts c " +
           "WHERE p.photographerProfile.id = :photographerId AND p.active = true")
    List<Long> findConceptIdsByPhotographerId(@Param("photographerId") Long photographerId);
}