package com.lunchbox.lunch_box.security.jwt;

import com.lunchbox.lunch_box.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("=== AuthTokenFilter called for URI: {} ===", request.getRequestURI());

        try {
            String jwt = parseJwt(request);
            logger.info("JWT extracted: {}", jwt != null ? "YES (length: " + jwt.length() + ")" : "NO");

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                logger.debug("JWT token is valid");
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                logger.debug("Username from token: {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.info("UserDetails loaded - Authorities: {}", userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Authentication set in SecurityContext");
            } else {
                logger.warn("JWT validation failed or JWT is null");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage(), e);
        }
        logger.info("=== AuthTokenFilter END - Calling next filter ===");
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        logger.info("Parsing JWT from request...");
        String jwtFromCookie = jwtUtils.getJwtFromCookie(request);
        logger.info("JWT from cookie: {}", jwtFromCookie != null ? "FOUND" : "NOT FOUND");
        if (jwtFromCookie != null) {
            return jwtFromCookie;
        }
        String jwtFromHeader = jwtUtils.getJwtFromHeader(request);
        logger.info("JWT from header: {}", jwtFromHeader != null ? "FOUND" : "NOT FOUND");
        if (jwtFromHeader != null) {
            return jwtFromHeader;
        }
        return null;
    }
}
