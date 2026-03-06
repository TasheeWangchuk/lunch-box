package com.lunchbox.lunch_box.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import com.lunchbox.lunch_box.common.dto.response.ApiResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String tokenError = (String) request.getAttribute("token_error");
        String message;

        if ("TokenExpired".equals(tokenError)) {
            message = "Token has expired";
        } else if ("InvalidToken".equals(tokenError)) {
            message = "Token is invalid";
        } else {
            message = "Authentication failed";
        }

        ApiResponse<Void> apiResponse = ApiResponse.error(401, message);

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }

}
