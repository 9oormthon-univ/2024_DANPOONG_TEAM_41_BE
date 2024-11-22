package com.allgoing.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 허용할 엔드포인트를 명시
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/health-check",
                                "/oauth2/callback/kakao", // 카카오 로그인 엔드포인트 인증 없이 허용
                                "/api/v1/store/**",
                                "/api/v1/review/**",
                                "/api/v1/traditional/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()); // CSRF 비활성화
        return http.build();
    }
}