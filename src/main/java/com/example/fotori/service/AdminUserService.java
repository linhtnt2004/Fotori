package com.example.fotori.service;

import com.example.fotori.common.enums.UserStatus;

public interface AdminUserService {

    void updateUserStatus(Long userId, UserStatus status);
}
