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

    @Column(name = "city", columnDefinition = "TEXT")
    String city;

    @Column(name = "equipment", columnDefinition = "TEXT")
    String equipment;

    @Column(name = "experience_years")
    Integer experienceYears;

    @Column(name = "average_rating")
    Double averageRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    ApprovalStatus approvalStatus;

    @Column(name = "approved_at")
    LocalDateTime approvedAt;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Column(name = "bank_name")
    String bankName;

    @Column(name = "bank_account_number")
    String bankAccountNumber;

    @Column(name = "bank_account_name")
    String bankAccountName;
}
