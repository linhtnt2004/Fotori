package com.example.fotori.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionPlanResponse {

    Long id;

    String name;

    Integer price;

    Integer durationDays;

    Integer commissionPercent;

    Integer maxPackages;

    Boolean aiSuggest;

    Boolean priorityListing;

    Boolean unlimitedChat;

    Boolean analytics;

    Boolean marketingTools;
}