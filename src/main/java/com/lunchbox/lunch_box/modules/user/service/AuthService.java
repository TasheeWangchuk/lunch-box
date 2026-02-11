package com.lunchbox.lunch_box.modules.user.service;

import com.lunchbox.lunch_box.modules.user.dto.AuthResponse;
import com.lunchbox.lunch_box.modules.user.dto.LoginRequest;
import com.lunchbox.lunch_box.modules.user.dto.RefreshTokenRequest;
import com.lunchbox.lunch_box.modules.user.dto.RegisterRequest;
import com.lunchbox.lunch_box.modules.user.dto.TokenData;

public interface AuthService {
    AuthResponse<TokenData> authenticateUser(LoginRequest loginRequest);

    AuthResponse<String> registerUser(RegisterRequest registerRequest);

    AuthResponse<TokenData> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
