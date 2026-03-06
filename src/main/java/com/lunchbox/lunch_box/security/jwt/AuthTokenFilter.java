package com.lunchbox.lunch_box.security.jwt;

import com.lunchbox.lunch_box.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        logger.debug("=== AuthTokenFilter called for URI: {} ===", request.getRequestURI());

        try {
            String jwt = parseJwt(request);

            if (jwt != null) {
                logger.debug("JWT found, validating token...");

                // validate token (throws exception if invalid/expired)
                jwtUtils.validateJwtToken(jwt);

                String subject = jwtUtils.getUserNameFromJwtToken(jwt);
                Long userId = Long.parseLong(subject);

                logger.debug("User ID from token: {}", userId);

                UserDetails userDetails = userDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Authentication set in SecurityContext");

            } else {
                logger.debug("No JWT token found in request");
            }

        } catch (ExpiredJwtException e) {

            logger.error("JWT token expired: {}", e.getMessage());
            request.setAttribute("token_error", "TokenExpired");

        } catch (JwtException e) {

            logger.error("Invalid JWT token: {}", e.getMessage());
            request.setAttribute("token_error", "InvalidToken");

        } catch (Exception e) {

            logger.error("Cannot set user authentication: {}", e.getMessage(), e);
            request.setAttribute("token_error", "InvalidToken");
        }

        logger.debug("=== AuthTokenFilter END - calling next filter ===");

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {

        logger.debug("Parsing JWT from request");

        String jwtFromCookie = jwtUtils.getJwtFromCookie(request);
        if (jwtFromCookie != null) {
            logger.debug("JWT found in cookie");
            return jwtFromCookie;
        }

        String jwtFromHeader = jwtUtils.getJwtFromHeader(request);
        if (jwtFromHeader != null) {
            logger.debug("JWT found in header");
            return jwtFromHeader;
        }

        logger.debug("JWT not found in cookie or header");

        return null;
    }
}
