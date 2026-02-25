package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "photographer_availability",
    indexes = {
        @Index(name = "idx_availability_photographer", columnList = "photographer_id"),
        @Index(name = "idx_availability_time", columnList = "start_time,end_time")
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotographerAvailability extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    PhotographerProfile photographer;

    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    LocalDateTime endTime;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;
}