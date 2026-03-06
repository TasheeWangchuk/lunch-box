package com.lunchbox.lunch_box.modules.auth.service;

import com.lunchbox.lunch_box.modules.auth.dto.request.GoogleLoginRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.LoginRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.RefreshTokenRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.RegisterRequest;
import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
import com.lunchbox.lunch_box.modules.auth.dto.response.TokenData;

public interface AuthService {
    ApiResponse<TokenData> authenticateWithGoogle(GoogleLoginRequest googleLoginRequest);

    ApiResponse<TokenData> authenticateUser(LoginRequest loginRequest);

    ApiResponse<String> registerUser(RegisterRequest registerRequest);

    ApiResponse<TokenData> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
