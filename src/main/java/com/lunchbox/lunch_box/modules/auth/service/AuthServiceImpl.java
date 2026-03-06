package com.lunchbox.lunch_box.modules.auth.service;

import com.lunchbox.lunch_box.modules.auth.dto.request.GoogleLoginRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.LoginRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.RefreshTokenRequest;
import com.lunchbox.lunch_box.modules.auth.dto.request.RegisterRequest;
import com.lunchbox.lunch_box.common.dto.response.ApiResponse;
import com.lunchbox.lunch_box.modules.auth.dto.response.TokenData;
import com.lunchbox.lunch_box.common.exception.BadRequestException;
import com.lunchbox.lunch_box.common.exception.ConflictException;
import com.lunchbox.lunch_box.common.exception.ResourceNotFoundException;
import com.lunchbox.lunch_box.common.exception.UnauthorizedException;
import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import com.lunchbox.lunch_box.modules.user.enums.AuthProvider;
import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.repository.UserRepository;
import com.lunchbox.lunch_box.security.jwt.JwtUtils;
import com.lunchbox.lunch_box.security.services.UserDetailsImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JwtDecoder googleJwtDecoder;

    @Override
    public ApiResponse<TokenData> authenticateWithGoogle(GoogleLoginRequest req) {
        try {
            Jwt googleJwt = googleJwtDecoder.decode(req.idToken());

            String googleSub = googleJwt.getSubject(); // provider_user_id
            String email = googleJwt.getClaimAsString("email");
            Boolean emailVerified = googleJwt.getClaimAsBoolean("email_verified");
            String name = googleJwt.getClaimAsString("name");
            String picture = googleJwt.getClaimAsString("picture");

            if (googleSub == null || googleSub.isBlank()) {
                throw new UnauthorizedException("Invalid Google token (missing sub).");
            }

            // 1) Find by provider+sub (best)
            Optional<User> byProvider = userRepository.findByProviderAndProviderUserId(AuthProvider.GOOGLE, googleSub);

            User user = byProvider.orElseGet(() -> {
                // Optional: If you want to link existing LOCAL account by email, you can do it
                // here.
                // For safety, only link if email is verified.
                if (email != null && Boolean.TRUE.equals(emailVerified)) {
                    Optional<User> existingByEmail = userRepository.findByEmail(email);
                    if (existingByEmail.isPresent()) {
                        User u = existingByEmail.get();
                        // link google to existing user
                        u.setProvider(AuthProvider.GOOGLE); // or keep LOCAL and store linked providers in another table
                        u.setProviderUserId(googleSub);
                        u.setEmailVerified(true);
                        if (u.getAvatarUrl() == null)
                            u.setAvatarUrl(picture);
                        return u;
                    }
                }

                User u = new User();
                u.setProvider(AuthProvider.GOOGLE);
                u.setProviderUserId(googleSub);
                u.setEmail(email);
                u.setEmailVerified(Boolean.TRUE.equals(emailVerified));
                u.setAvatarUrl(picture);

                // Username strategy: generate if missing
                u.setUsername(makeUsername(name, email));
                u.setPassword(null); // IMPORTANT for google users
                u.setRole(AppRole.CUSTOMER);
                u.setActive(true);
                u.setCreatedAt(OffsetDateTime.now()); // if you use @CreationTimestamp, you can remove this
                return u;
            });

            // Update “latest” fields
            if (email != null)
                user.setEmail(email);
            if (picture != null)
                user.setAvatarUrl(picture);
            if (Boolean.TRUE.equals(emailVerified))
                user.setEmailVerified(true);
            user.setLastLoginAt(OffsetDateTime.now());

            user = userRepository.save(user);

            // 2) Issue YOUR tokens (access + refresh)
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            String accessToken = jwtUtils.generateAccessToken(userDetails);
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            TokenData tokenData = new TokenData(accessToken, refreshToken);

            return ApiResponse.success(200, "Google login successful", tokenData);

        } catch (Exception e) {
            log.error("Google authentication failed", e);
            throw new UnauthorizedException("Google authentication failed (invalid token).");
        }
    }

    @Override
    public ApiResponse<TokenData> authenticateUser(LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail().trim().toLowerCase();

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User u = userOpt.get();

                // Google account → block password login
                if (u.getProvider() == AuthProvider.GOOGLE) {
                    throw new ConflictException("Please sign in with Google.");
                }

                if (u.getProvider() == AuthProvider.LOCAL &&
                        (u.getPassword() == null || u.getPassword().isBlank())) {
                    throw new ConflictException(
                            "Password login isn’t enabled for this account. Please reset your password or use Google.");
                }
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setLastLoginAt(OffsetDateTime.now());
            userRepository.save(user);

            String accessToken = jwtUtils.generateAccessToken(userDetails);
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            return ApiResponse.success(200, "Login successful", new TokenData(accessToken, refreshToken));

        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    @Override
    public ApiResponse<String> registerUser(RegisterRequest registerRequest) {
        try {
            String email = registerRequest.getEmail().trim().toLowerCase();

            Optional<User> existing = userRepository.findByEmail(email);
            if (existing.isPresent()) {
                User u = existing.get();

                if (u.getProvider() == AuthProvider.GOOGLE) {
                    throw new ConflictException(
                            "An account with this email already exists using Google. Please continue with Google sign-in.");
                }
                throw new ConflictException("Email is already registered");
            }

            if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
                throw new ConflictException("Username is already taken");
            }

            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                throw new BadRequestException("Passwords do not match");
            }

            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setPhone(registerRequest.getPhone());
            user.setProvider(AuthProvider.LOCAL);
            user.setProviderUserId(null);
            user.setEmailVerified(false);
            user.setRole(AppRole.CUSTOMER);
            user.setActive(true);

            userRepository.save(user);

            return ApiResponse.success(200, "User registered successfully", "Registration complete");

        } catch (Exception e) {
            log.error("Registration failed for user: {}", registerRequest.getEmail(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<TokenData> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        // jwtUtils.getUserIdFromJwtToken will throw ExpiredJwtException or similar if
        // invalid
        Long userId = jwtUtils.getUserIdFromJwtToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getActive()) {
            throw new UnauthorizedException("User account is deactivated");
        }

        // Build UserDetails and generate new tokens
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String newAccessToken = jwtUtils.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);

        TokenData tokenData = new TokenData(newAccessToken, newRefreshToken);
        return ApiResponse.success(200, "Token refreshed successfully", tokenData);
    }

    private String makeUsername(String name, String email) {
        if (name != null && !name.isBlank())
            return name.replaceAll("\\s+", "").toLowerCase();
        if (email != null)
            return email.split("@")[0];
        return "user" + System.currentTimeMillis();
    }
}
