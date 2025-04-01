package com.dodo.smartsafereturn.global.handler;

import com.dodo.smartsafereturn.safeRoute.entity.RouteState;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // SafeRouteController @PutMapping("/{safeRouteId}/state") 및 관련 매개변수 에러 핸들링
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, Object> response = new HashMap<>();
        // response : errorCode, message, validValues 필드로 에러 정보 JSON 형식으로 구체화

        log.error("HttpMessageNotReadableException: {}", e.getMessage());

        // RouteState enum 관련 오류인지 확인
        if (e.getMessage() != null && e.getMessage().contains("RouteState")) {
            response.put("errorCode", "INVALID_ENUM_VALUE");
            response.put("message", "유효하지 않은 상태값입니다");
            response.put("validValues", Arrays.stream(RouteState.values())
                    .map(Enum::name)
                    .toArray());

            return ResponseEntity.badRequest().body(response);
        }

        response.put("errorCode", "INVALID_REQUEST_FORMAT");
        response.put("message", "잘못된 요청 형식입니다");

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.error("파일 크기 초과 오류: ", e);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("파일 크기가 제한을 초과했습니다. 최대 10MB 까지 업로드할 수 있습니다.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error("런타임 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
