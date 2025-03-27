package com.dodo.smartsafereturn.global.config;

import com.dodo.smartsafereturn.auth.repository.RefreshTokenRepository;
import com.dodo.smartsafereturn.auth.service.AuthService;
import com.dodo.smartsafereturn.auth.utils.CustomLogoutFilter;
import com.dodo.smartsafereturn.auth.utils.JwtFilter;
import com.dodo.smartsafereturn.auth.utils.JwtUtil;
import com.dodo.smartsafereturn.auth.utils.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthService authService;

    @Value("${jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(
                        cors ->
                                cors.configurationSource(corsConfigurationSource())
                )
                .sessionManagement(
                        session ->
                                session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                        .maximumSessions(1)
                                        .maxSessionsPreventsLogin(false)
                )
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                        .requestMatchers("/api/auth/login", "/api/auth/logout").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/auth/reissue").permitAll() // 리프레시 토큰 재발급 로직 경로
                                        .requestMatchers(HttpMethod.POST, "/api/member").permitAll()
                                        .requestMatchers("/api/test").permitAll()
                                        .requestMatchers("/api/test/jwtTest1").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("/api/**").authenticated()
                                        .anyRequest().permitAll()
                )
                // 시큐리티의 아이디 비밀번호 인증 필터 대신 인증 및 jwt 발급하는 custom 필터 사용
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),
                                jwtUtil, accessExpiration, refreshExpiration, refreshTokenRepository),
                        UsernamePasswordAuthenticationFilter.class)
                // jwt 필터 체인
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                // 로그아웃 필터 등록 : 기존 시큐리티의 LogoutFilter 필터보다 먼저 수행되도록 하기
                // LogoutFilter 은 여전히 존재하는 시큐리티 컨텍스트의 인증정보을 지우기 위해 존재해야함
                .addFilterBefore(new CustomLogoutFilter(authService, jwtUtil), LogoutFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(allowedOrigins);
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
