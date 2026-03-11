package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import com.example.fotori.common.enums.PaymentMethod;
import com.example.fotori.common.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    Booking booking;

    Double amount;

    @Enumerated(EnumType.STRING)
    PaymentMethod method;

    String transactionId;

    String qrContent;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;
}
