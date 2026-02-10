package com.example.fotori.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "staffs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long staffId;

    @Column(unique = true, nullable = false)
    String email;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(nullable = false)
    String role;

    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDate createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDate updateAt;
}
