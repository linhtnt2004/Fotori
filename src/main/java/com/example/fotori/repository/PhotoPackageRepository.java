package com.example.fotori.repository;

import com.example.fotori.model.PhotoPackage;
import com.example.fotori.model.PhotographerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoPackageRepository extends JpaRepository<PhotoPackage, Long> {

    List<PhotoPackage> findByPhotographerProfileAndActiveTrue(PhotographerProfile photographerProfile);

    List<PhotoPackage> findByActiveTrue();
}
