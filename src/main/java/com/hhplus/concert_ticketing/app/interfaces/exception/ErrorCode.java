package com.hhplus.concert_ticketing.app.interfaces.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    QUEUE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "대기열을 찾을 수 없습니다."),
    QUEUE_FULL_ERROR(HttpStatus.FORBIDDEN, "대기열이 가득 찼습니다."),
    QUEUE_EXPIRED_ERROR(HttpStatus.FORBIDDEN, "대기열 대기 시간이 만료되었습니다."),
    QUEUE_TOKEN_INVALID_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 대기열 토큰입니다."),
    QUEUE_POSITION_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "대기열에서 해당 위치를 찾을 수 없습니다."),
    QUEUE_ALREADY_IN_PROGRESS_ERROR(HttpStatus.CONFLICT, "이미 대기 중인 상태입니다."),
    QUEUE_ACCESS_DENIED_ERROR(HttpStatus.FORBIDDEN, "대기열에 접근할 권한이 없습니다."),
    QUEUE_RESERVATION_FAILED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "대기열에서 좌석 예약에 실패했습니다."),
    QUEUE_CAPACITY_LIMIT_REACHED_ERROR(HttpStatus.FORBIDDEN, "대기열의 수용 한도에 도달했습니다."),
    QUEUE_SERVICE_UNAVAILABLE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "대기열 서비스가 일시적으로 중단되었습니다."),
    PERFORMANCE_SOLD_OUT_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "대기열 서비스가 일시적으로 중단되었습니다."),
    INVALID_PERFORMANCE_ERROR(HttpStatus.BAD_REQUEST, "잘못된 공연 정보가 제공되었습니다."),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INSUFFICIENT_POINT_ERROR(HttpStatus.INSUFFICIENT_STORAGE,"포인트가 부족합니다."),
    RESERVE_CANCEL_ERROR(HttpStatus.CONFLICT , "예약이 취소된 상태입니다.");


    private final HttpStatus code;
    private final String message;
}
