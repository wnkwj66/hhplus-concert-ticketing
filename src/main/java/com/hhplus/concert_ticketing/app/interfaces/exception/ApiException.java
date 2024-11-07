package com.hhplus.concert_ticketing.app.interfaces.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

    private final LogLevel logLevel;

    public ApiException(ErrorCode errorCode, LogLevel logLevel) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.logLevel = logLevel;
    }
}
