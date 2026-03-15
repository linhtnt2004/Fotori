package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    Integer price;

    @Column(nullable = false)
    Integer durationDays;

    @Column(nullable = false)
    Integer commissionPercent;

    @Column(nullable = false)
    Integer maxPackages;

    @Column(nullable = false)
    Boolean aiSuggest;

    @Column(nullable = false)
    Boolean priorityListing;

    @Column(nullable = false)
    Boolean unlimitedChat;

    @Column(nullable = false)
    Boolean analytics;

    @Column(nullable = false)
    Boolean marketingTools;

    @Column(nullable = false)
    Boolean active;
}