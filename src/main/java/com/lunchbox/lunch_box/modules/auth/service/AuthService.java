package com.lunchbox.lunch_box.modules.auth.service;

import com.lunchbox.lunch_box.modules.auth.dto.request.GoogleLoginRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.LoginRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.RefreshTokenRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.RegisterRequest;
import com.lunchbox.lunch_box.modules.auth.dto.response.AuthResponse;
import com.lunchbox.lunch_box.modules.auth.dto.response.TokenData;

public interface AuthService {
    AuthResponse<TokenData> authenticateWithGoogle(GoogleLoginRequest googleLoginRequest);

    AuthResponse<TokenData> authenticateUser(LoginRequest loginRequest);

    AuthResponse<String> registerUser(RegisterRequest registerRequest);

    AuthResponse<TokenData> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
