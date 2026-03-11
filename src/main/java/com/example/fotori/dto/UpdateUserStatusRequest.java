package com.example.fotori.dto;

import com.example.fotori.common.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserStatusRequest {

    UserStatus status;

}