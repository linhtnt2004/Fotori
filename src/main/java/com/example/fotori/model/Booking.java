package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.common.enums.BookingStatus;
import com.example.fotori.common.resolver.BookingStatusResolver;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    PhotographerProfile photographer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_package_id", nullable = false)
    PhotoPackage photoPackage;

    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    LocalDateTime endTime;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_status", nullable = false)
    BookingActorStatus customerStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "photographer_status", nullable = false)
    BookingActorStatus photographerStatus;

    @Column(name = "note", columnDefinition = "TEXT")
    String note;

    public void refreshStatus() {
        this.status = BookingStatusResolver.resolve(
            this.customerStatus,
            this.photographerStatus
        );
    }
}
