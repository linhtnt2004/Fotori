package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photo_packages")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotoPackage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    PhotographerProfile photographerProfile;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "price", nullable = false)
    Integer price;

    @Column(name = "duration_minutes", nullable = false)
    Integer durationMinutes;

    @ManyToMany
    @JoinTable(
        name = "photo_package_concepts",
        joinColumns = @JoinColumn(name = "photo_package_id"),
        inverseJoinColumns = @JoinColumn(name = "photo_concept_id")
    )
    @Builder.Default
    private Set<PhotoConcept> concepts = new HashSet<>();

    @Column(name = "active", nullable = false)
    Boolean active;
}
