package com.lunchbox.lunch_box.modules.user.service;

import com.lunchbox.lunch_box.modules.user.dto.*;

public interface AuthService {
    AuthResponse<TokenData> authenticateWithGoogle(GoogleLoginRequest googleLoginRequest);

    AuthResponse<TokenData> authenticateUser(LoginRequest loginRequest);

    AuthResponse<String> registerUser(RegisterRequest registerRequest);

    AuthResponse<TokenData> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
