package com.example.fotori.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long customerId;

    @Column(unique = true, nullable = false)
    String email;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(length = 10)
    String gender;

    @Column(name = "birth_date")
    LocalDate birthDate;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updateAt;
}
