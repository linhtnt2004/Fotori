package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.common.enums.UserStatus;
import com.example.fotori.dto.*;
import com.example.fotori.model.RefreshToken;
import com.example.fotori.model.User;
import com.example.fotori.security.JwtTokenProvider;
import com.example.fotori.security.UserDetailsImpl;
import com.example.fotori.service.AuthService;
import com.example.fotori.service.EmailVerificationService;
import com.example.fotori.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Tag(name = "Authentication", description = "Auth APIs for login/register/refresh/logout")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailService;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);

            Map<String, Object> data = Map.of(
                "user", user
            );

            return ResponseEntity.ok(
                new ApiResponse(
                    ErrorCode.SUCCESS.name(),
                    "User register successfully!",
                    data
                )
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(
                new ApiResponse(
                    ErrorCode.BAD_REQUEST.name(),
                    e.getMessage(),
                    null
                )
            );
        }
    }

    @Operation(summary = "Login user and return access token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
        @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {

        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getStatus() == UserStatus.PENDING) {
            return ResponseEntity.status(403).body(
                new ApiResponse(
                    ErrorCode.FORBIDDEN.name(),
                    "Please verify your email before login",
                    null
                )
            );
        }

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);

        RefreshToken refreshToken =
            refreshTokenService.create(((UserDetailsImpl) userDetails).getUser());

        Cookie cookie = new Cookie("refresh_token", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge((int) (refreshExpirationMs / 1000));

        response.addCookie(cookie);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Login successful",
                new LoginResponse(accessToken)
            )
        );
    }

    @Operation(summary = "Refresh access token using refresh token cookie")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(
        HttpServletRequest request
    ) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(401).body(
                new ApiResponse(
                    ErrorCode.UNAUTHORIZED.name(),
                    "Refresh token not found",
                    null
                )
            );
        }

        String refreshTokenStr = null;

        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshTokenStr = cookie.getValue();
                break;
            }
        }

        if (refreshTokenStr == null) {
            return ResponseEntity.status(401).body(
                new ApiResponse(
                    ErrorCode.UNAUTHORIZED.name(),
                    "Refresh token not found",
                    null
                )
            );
        }

        RefreshToken refreshToken =
            refreshTokenService.verify(refreshTokenStr);

        User user = refreshToken.getUser();

        UserDetails userDetails = new UserDetailsImpl(user);

        String accessToken =
            jwtTokenProvider.generateAccessToken(userDetails);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Token refreshed",
                new LoginResponse(accessToken)
            )
        );
    }

    @Operation(summary = "Logout and revoke refresh token")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if ("refresh_token".equals(cookie.getName())) {

                    refreshTokenService.revoke(cookie.getValue());

                    cookie.setValue("");
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true);
                    cookie.setPath("/api/auth");
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);

                    break;
                }
            }
        }

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Logout successful",
                null
            )
        );
    }

    @Operation(
        summary = "Verify user email",
        description = "Verifies the user's email address using the unique token sent to their inbox."
    )
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(
        @RequestParam String token
    ) {

        emailService.verify(token);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Email verified successfully",
                null
            )
        );
    }

    @Operation(summary = "Request password reset")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
        @RequestBody ResetPasswordRequest request
    ) {

        authService.resetPassword(request.getEmail());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Password reset email sent",
                null
            )
        );
    }

    @Operation(summary = "Reset password with token")
    @PostMapping("/new-password")
    public ResponseEntity<ApiResponse> newPassword(
        @RequestBody NewPasswordRequest request
    ) {

        authService.resetPasswordWithToken(
            request.getToken(),
            request.getNewPassword()
        );

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Password reset successfully",
                null
            )
        );
    }

    @Operation(summary = "Get current user info")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        UserResponse user =
            authService.getCurrentUser(userDetails.getUsername());

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Current user info",
                java.util.Map.of("user", user)
            )
        );
    }

    @Operation(summary = "Update current user info")
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody UpdateProfileRequest request
    ) {

        UserResponse user =
            authService.updateProfile(userDetails.getUsername(), request);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Profile updated successfully",
                java.util.Map.of("user", user)
            )
        );
    }
}