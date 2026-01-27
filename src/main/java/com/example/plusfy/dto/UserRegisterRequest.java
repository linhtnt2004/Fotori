package com.example.plusfy.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String fullName;
    private String password;
    private String email;
    private String phone;
}
