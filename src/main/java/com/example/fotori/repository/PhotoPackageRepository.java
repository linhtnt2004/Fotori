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

    List<PhotoPackage> findByPhotographerProfileAndActiveTrue(PhotographerProfile photographerProfile);
    List<PhotoPackage> findByActiveTrue();
    List<PhotoPackage> findByPhotographerProfileIdAndActiveTrue(Long photographerId);

    @Query("SELECT MIN(p.price) FROM PhotoPackage p " +
           "WHERE p.photographerProfile.id = :photographerId AND p.active = true")
    Integer findMinPriceByPhotographerId(@Param("photographerId") Long photographerId);

    @Query("SELECT MAX(p.price) FROM PhotoPackage p " +
           "WHERE p.photographerProfile.id = :photographerId AND p.active = true")
    Integer findMaxPriceByPhotographerId(@Param("photographerId") Long photographerId);

    @Query("SELECT DISTINCT c.name FROM PhotoPackage p " +
           "JOIN p.concepts c " +
           "WHERE p.photographerProfile.id = :photographerId AND p.active = true")
    List<String> findConceptNamesByPhotographerId(@Param("photographerId") Long photographerId);

    @Query("SELECT DISTINCT c.id FROM PhotoPackage p " +
           "JOIN p.concepts c " +
           "WHERE p.photographerProfile.id = :photographerId AND p.active = true")
    List<Long> findConceptIdsByPhotographerId(@Param("photographerId") Long photographerId);

    void deleteByPhotographerProfile(PhotographerProfile photographerProfile);
}