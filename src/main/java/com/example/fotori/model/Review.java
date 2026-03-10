package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    PhotographerProfile photographerProfile;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    Booking booking;

    Integer rating;

    Integer skills;

    Integer attitude;

    Integer punctuality;

    Integer postProcessing;

    @Column(columnDefinition = "TEXT")
    String comment;

    String response;
}