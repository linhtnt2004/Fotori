package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import com.example.fotori.common.enums.ApprovalStatus;
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
@Table(name = "photographers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotographerProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Column(name = "bio", columnDefinition = "TEXT")
    String bio;

    @Column(name = "experience_years")
    Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    ApprovalStatus approvalStatus;

    @Column(name = "approved_at")
    LocalDateTime approvedAt;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;
}
