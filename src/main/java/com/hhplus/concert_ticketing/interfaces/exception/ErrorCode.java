package com.hhplus.concert_ticketing.interfaces.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    QUEUE_NOT_FOUND_ERROR("NOT_FOUND", "대기열을 찾을 수 없습니다."),
    QUEUE_FULL_ERROR("FORBIDDEN", "대기열이 가득 찼습니다."),
    QUEUE_EXPIRED_ERROR("FORBIDDEN", "대기열 대기 시간이 만료되었습니다."),
    QUEUE_TOKEN_INVALID_ERROR("UNAUTHORIZED", "유효하지 않은 대기열 토큰입니다."),
    QUEUE_POSITION_NOT_FOUND_ERROR("NOT_FOUND", "대기열에서 해당 위치를 찾을 수 없습니다."),
    QUEUE_ALREADY_IN_PROGRESS_ERROR("CONFLICT", "이미 대기 중인 상태입니다."),
    QUEUE_ACCESS_DENIED_ERROR("FORBIDDEN", "대기열에 접근할 권한이 없습니다."),
    QUEUE_RESERVATION_FAILED_ERROR("INTERNAL_SERVER_ERROR", "대기열에서 좌석 예약에 실패했습니다."),
    QUEUE_CAPACITY_LIMIT_REACHED_ERROR("FORBIDDEN", "대기열의 수용 한도에 도달했습니다."),
    QUEUE_SERVICE_UNAVAILABLE_ERROR("SERVICE_UNAVAILABLE", "대기열 서비스가 일시적으로 중단되었습니다."),
    INVALID_PERFORMANCE_ERROR("BAD_REQUEST", "잘못된 공연 정보가 제공되었습니다.");

    private final String code;
    private final String message;
}
