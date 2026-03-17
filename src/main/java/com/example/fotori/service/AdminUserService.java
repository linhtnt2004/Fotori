package com.example.fotori.service;

import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.dto.ApiUserDetailResponse;

public interface AdminUserService {

    void updateUserStatus(Long userId, UserStatus status);

    ApiUserDetailResponse getUserDetail(Long userId);

    void deleteUser(Long userId);

    void deleteUserHard(Long userId);
}
