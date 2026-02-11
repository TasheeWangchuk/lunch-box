package com.lunchbox.lunch_box.modules.user.service;

import com.lunchbox.lunch_box.modules.user.dto.AuthResponse;
import com.lunchbox.lunch_box.modules.user.dto.RefreshTokenRequest;
import com.lunchbox.lunch_box.modules.user.dto.TokenData;
import com.lunchbox.lunch_box.modules.user.entity.AppRole;
import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.repository.UserRepository;
import com.lunchbox.lunch_box.security.jwt.JwtUtils;
import com.lunchbox.lunch_box.modules.user.dto.LoginRequest;
import com.lunchbox.lunch_box.modules.user.dto.RegisterRequest;
import com.lunchbox.lunch_box.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public AuthResponse<TokenData> authenticateUser(LoginRequest loginRequest) {
        try {
            // Authenticate user using email
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Update last login timestamp
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setLastLoginAt(OffsetDateTime.now());
            userRepository.save(user);

            // Generate tokens
            String accessToken = jwtUtils.generateAccessToken(userDetails);
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            TokenData tokenData = new TokenData(accessToken, refreshToken);
            return AuthResponse.success("Login successful", tokenData);

        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            return AuthResponse.error("Invalid email or password");
        }
    }

    @Override
    public AuthResponse<String> registerUser(RegisterRequest registerRequest) {
        try {
            // Check if email already exists
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                return AuthResponse.error("Email is already registered");
            }
            // Check if username already exists
            if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
                return AuthResponse.error("Username is already taken");
            }

            // Validate password confirmation
            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                return AuthResponse.error("Passwords do not match");
            }

            // Create new user
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setPhone(registerRequest.getPhone());

            // Set role based on request or default to CUSTOMER
            if (registerRequest.getRole() != null && !registerRequest.getRole().isEmpty()) {
                try {
                    user.setRole(AppRole.valueOf(registerRequest.getRole().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    user.setRole(AppRole.CUSTOMER);
                }
            } else {
                user.setRole(AppRole.CUSTOMER);
            }

            user.setActive(true);

            userRepository.save(user);
            log.info("User registered successfully: {}", user.getEmail());

            return AuthResponse.success("User registered successfully", "Registration complete");

        } catch (Exception e) {
            log.error("Registration failed for user: {}", registerRequest.getEmail(), e);
            return AuthResponse.error("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse<TokenData> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            String refreshToken = refreshTokenRequest.getRefreshToken();

            // Validate refresh token
            if (!jwtUtils.validateJwtToken(refreshToken)) {
                return AuthResponse.error("Invalid or expired refresh token");
            }

            // Get user ID from token
            Long userId = jwtUtils.getUserIdFromJwtToken(refreshToken);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if user is still active
            if (!user.getActive()) {
                return AuthResponse.error("User account is deactivated");
            }

            // Build UserDetails and generate new tokens
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            String newAccessToken = jwtUtils.generateAccessToken(userDetails);
            String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);

            TokenData tokenData = new TokenData(newAccessToken, newRefreshToken);
            return AuthResponse.success("Token refreshed successfully", tokenData);

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return AuthResponse.error("Failed to refresh token: " + e.getMessage());
        }
    }
}
