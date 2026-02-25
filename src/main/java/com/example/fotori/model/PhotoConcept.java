package com.example.fotori.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photo_concepts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotoConcept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;
}
