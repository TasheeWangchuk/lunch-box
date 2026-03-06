package com.lunchbox.lunch_box.modules.auth.controller;

import com.lunchbox.lunch_box.modules.auth.dto.request.GoogleLoginRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.LoginRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.RefreshTokenRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.RegisterRequest;
import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
import com.lunchbox.lunch_box.modules.auth.dto.response.TokenData;
import com.lunchbox.lunch_box.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<TokenData>> google(@Valid @RequestBody GoogleLoginRequest req) {
        ApiResponse<TokenData> response = authService.authenticateWithGoogle(req);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenData>> login(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<TokenData> response = authService.authenticateUser(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse<String> response = authService.registerUser(registerRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenData>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        ApiResponse<TokenData> response = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
