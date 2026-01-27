package com.example.plusfy.security;

import com.example.plusfy.common.ApiResponse;
import com.example.plusfy.common.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ApiResponse apiResponse = new ApiResponse(
        ErrorCode.INVALID_CREDENTIALS.name(),
        "Invalid username or password",
        null
        );

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}
