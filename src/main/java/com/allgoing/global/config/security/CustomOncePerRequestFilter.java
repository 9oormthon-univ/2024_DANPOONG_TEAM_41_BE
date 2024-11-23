package com.allgoing.global.config.security;

import com.allgoing.global.config.security.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtParser jwtParser;

    @Autowired
    public CustomOncePerRequestFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, @Value("${app.auth.tokenSecret}") String jwtSecret) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = null;
            try {
                username = jwtTokenUtil.getUsernameFromJWT(jwt);
            } catch (ExpiredJwtException ex) {
                String refreshToken = getRefreshTokenFromRequest(request);
                if (refreshToken != null && jwtTokenUtil.validateRefreshToken(refreshToken)) {
                    String newAccessToken = jwtTokenUtil.generateToken(new HashMap<>(), jwtTokenUtil.getUsernameFromRefreshToken(refreshToken));
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    username = jwtTokenUtil.getUsernameFromJWT(newAccessToken);
                }
            }

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

//    private void processToken(String jwt, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String email = jwtTokenUtil.getEmailFromJWT(jwt); // 이메일 추출
//            log.info("Extracted email from JWT: {}", email);
//            authenticateUser(email, jwt, request);
//        } catch (ExpiredJwtException ex) {
//            handleExpiredToken(ex, request, response);
//        }
//    }

    private void authenticateUser(String email, String jwt, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email); // 이메일로 사용자 검색
        if (jwtTokenUtil.validateToken(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("User authenticated successfully: {}", email);
        } else {
            log.warn("JWT validation failed for token: {}", jwt);
        }
    }


    private void handleExpiredToken(ExpiredJwtException ex, HttpServletRequest request, HttpServletResponse response) {
        log.warn("JWT expired: {}", ex.getMessage());
        String refreshToken = getRefreshTokenFromRequest(request);
        if (refreshToken != null && jwtTokenUtil.validateRefreshToken(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromRefreshToken(refreshToken);
            log.info("Refreshing token for username: {}", username);
            String newAccessToken = jwtTokenUtil.generateToken(new HashMap<>(), username);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            authenticateUser(username, newAccessToken, request);
        } else {
            log.warn("Invalid or missing refresh token for expired JWT.");
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Refresh-Token");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
