package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import com.example.fotori.common.enums.VoucherType;
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
@Table(name = "vouchers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher extends BaseEntity {

    @Id
    @Column(name = "code", nullable = false, unique = true)
    String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    VoucherType type;

    @Column(name = "value", nullable = false)
    Integer value;

    @Column(name = "min_order_value")
    Integer minOrderValue;

    @Column(name = "max_discount")
    Integer maxDiscount;

    @Column(name = "starts_at")
    LocalDateTime startsAt;

    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;

    @Column(name = "usage_limit")
    Integer usageLimit;

    @Column(name = "used_count", nullable = false)
    Integer usedCount;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "active", nullable = false)
    Boolean active;
}