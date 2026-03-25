package com.example.fotori.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VoucherType {

    PERCENTAGE,
    FIXED;

    @JsonCreator
    public static VoucherType fromString(String value) {
        if (value == null) return null;
        for (VoucherType type : VoucherType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

}