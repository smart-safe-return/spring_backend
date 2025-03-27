package com.dodo.smartsafereturn.auth.controller;

import com.dodo.smartsafereturn.auth.dto.RefreshValidationResultDto;
import com.dodo.smartsafereturn.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT Refresh Token 관련 인증 기능
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // refresh 헤더에서 리프레시 토큰 가져오기
        String refreshToken = request.getHeader("refresh");
        // 재발급 로직
        RefreshValidationResultDto result = authService.reissue(refreshToken, response);

        return new ResponseEntity<>(result.getBody(), result.getStatus());

    }
}
