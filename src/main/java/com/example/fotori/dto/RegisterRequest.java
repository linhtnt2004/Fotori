package com.example.fotori.dto;

import com.example.fotori.common.enums.RegisterType;
import lombok.Data;

import java.util.List;

@Data
public class RegisterRequest {

    private String email;

    private String password;

    private String fullName;

    private String phone;

    private RegisterType userType;

    private String bio;

    private List<String> specialties;

    private String city;

    private Integer experience;
}