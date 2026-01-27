package com.example.plusfy.security;

import com.example.plusfy.common.ApiResponse;
import com.example.plusfy.common.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        ApiResponse apiResponse = new ApiResponse(
        ErrorCode.ACCESS_DENIED.name(),
        "Access Denied!",
        null
        );

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}