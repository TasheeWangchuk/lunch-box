package com.lunchbox.lunch_box.modules.user.service;

import com.lunchbox.lunch_box.modules.user.dto.AuthResponse;
import com.lunchbox.lunch_box.modules.user.dto.RefreshTokenRequest;
import com.lunchbox.lunch_box.modules.user.dto.TokenData;
import com.lunchbox.lunch_box.security.request.LoginRequest;
import com.lunchbox.lunch_box.security.request.SignupRequest;

public interface AuthService {
    AuthResponse<TokenData> authenticateUser(LoginRequest loginRequest);

    AuthResponse<String> registerUser(SignupRequest signupRequest);

    AuthResponse<TokenData> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
