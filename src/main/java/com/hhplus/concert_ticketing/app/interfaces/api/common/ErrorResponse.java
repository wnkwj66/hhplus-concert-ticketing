package com.hhplus.concert_ticketing.app.interfaces.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String errorCode;
    private final String message;

}
