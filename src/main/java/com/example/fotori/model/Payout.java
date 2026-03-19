package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import com.example.fotori.common.enums.PayoutStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payouts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payout extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    Booking booking;

    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    PhotographerProfile photographer;

    Double totalAmount;

    Double commission;

    Double payoutAmount;

    @Enumerated(EnumType.STRING)
    PayoutStatus status;
}
