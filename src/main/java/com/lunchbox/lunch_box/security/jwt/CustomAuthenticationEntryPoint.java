package com.lunchbox.lunch_box.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
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

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", "Unauthorized");
        responseData.put("message", message);
        responseData.put("timestamp", System.currentTimeMillis());
        responseData.put("status", 401);

        new ObjectMapper().writeValue(response.getOutputStream(), responseData);
    }

}

