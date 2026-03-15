package com.example.fotori.dto;

import com.example.fotori.common.enums.PaymentMethod;
import lombok.Data;

@Data
public class PurchaseSubscriptionRequest {

    Long planId;

    PaymentMethod method;
}