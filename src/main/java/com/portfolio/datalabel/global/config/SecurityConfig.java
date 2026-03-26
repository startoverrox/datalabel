package com.portfolio.datalabel.global.config;

import com.portfolio.datalabel.global.security.JwtAuthenticationFilter;
import com.portfolio.datalabel.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // point: "기존 취약했던 SHA-256 대신, Salt가 자동으로 부여되는 BCrypt를 도입해 보안을 강화했습니다."
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // REST API이므로 CSRF 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                // 세션을 사용하지 않고 JWT를 사용할 것이므로 STATELESS로 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // 가입, 로그인, 스웨거는 통과
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                // UsernamePasswordAuthenticationFilter 앞에 커스텀 JWT 필터를 끼워넣음
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
