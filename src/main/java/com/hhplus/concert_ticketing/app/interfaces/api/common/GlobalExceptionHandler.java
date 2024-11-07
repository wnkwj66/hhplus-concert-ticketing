package com.hhplus.concert_ticketing.app.interfaces.api.common;

import com.hhplus.concert_ticketing.app.interfaces.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j // 로깅에 대한 추상 레이어를 제공하는 인터페이스의 모음
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        // 로그 레벨에 따라 로그 처리
        if (e.getLogLevel() == LogLevel.ERROR) {
            log.error("ApiException occurred: {}", e.getMessage());
        } else if (e.getLogLevel() == LogLevel.WARN) {
            log.warn("ApiException occurred: {}", e.getMessage());
        } else {
            log.info("ApiException occurred: {}", e.getMessage());
        }

        // 에러 코드와 메시지로 응답을 생성
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleApiException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("INVALID_ARGUMENT", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleApiException(NullPointerException e) {
        log.error("NullPointerException : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("NULL_POINTER", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Unexpected exception : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
