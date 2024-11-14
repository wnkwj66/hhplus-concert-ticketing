package com.hhplus.concert_ticketing.app.interfaces.api.common;

import com.hhplus.concert_ticketing.app.interfaces.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final HttpStatus errorCode;
    private final String message;

}
