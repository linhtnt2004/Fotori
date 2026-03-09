package com.example.fotori.dto;

import lombok.Data;

@Data
public class NewPasswordRequest {

    private String token;

    private String newPassword;

}