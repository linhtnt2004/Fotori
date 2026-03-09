package com.example.fotori.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {

    String name;

    String phone;

    String avatar;

    String gender;

    LocalDate birthDate;

    String address;

}