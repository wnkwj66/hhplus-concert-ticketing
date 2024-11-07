package com.hhplus.concert_ticketing.app.interfaces.api.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 *  GOAL :
 *  ResponseEntity 형식으로 반환하면 예외가 발생하는 경우 응답의 모양이 달라지기 때문에
 *  성공했을 때와 실패했을 때 어떠한 처리 결과에도 동일한 포맷의 응답을 리턴하도록 통일
 */
// @Schma : API 모델의 속성을 정의하고 문서화하는 데 사용
public record ApiResponse<T>(
        @Schema(description = "반환 결과")
        int status,
        @Schema(description = "반환 데이터")
        T data,
        @Schema(description = "반환 메시지")
        String message
) {
    // successWithNoContent
    public static ApiResponse<?> success() {
        return new ApiResponse<>(HttpStatus.OK.value(), null, "SUCCESS");
    }
    // successResponse
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, "SUCCESS");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, message);
    }

    public static ApiResponse<?> error(Exception e) {
        return new ApiResponse<>(HttpStatus.OK.value(), new HashMap<>(), e.getMessage());
    }

}
