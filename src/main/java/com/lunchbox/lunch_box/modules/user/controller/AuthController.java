package com.lunchbox.lunch_box.modules.user.controller;

import com.lunchbox.lunch_box.modules.user.dto.AuthResponse;
import com.lunchbox.lunch_box.modules.user.dto.RefreshTokenRequest;
import com.lunchbox.lunch_box.modules.user.dto.TokenData;
import com.lunchbox.lunch_box.modules.user.service.AuthService;
import com.lunchbox.lunch_box.security.request.LoginRequest;
import com.lunchbox.lunch_box.security.request.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse<TokenData>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse<TokenData> response = authService.authenticateUser(loginRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse<String>> register(@Valid @RequestBody SignupRequest signupRequest) {
        AuthResponse<String> response = authService.registerUser(signupRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse<TokenData>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse<TokenData> response = authService.refreshToken(refreshTokenRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
}
