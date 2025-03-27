package com.dodo.smartsafereturn.auth.service;

import com.dodo.smartsafereturn.auth.dto.RefreshValidationResultDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    RefreshValidationResultDto reissue(String refreshToken, HttpServletResponse response);
    void logout(String refreshToken, HttpServletResponse response);
}
