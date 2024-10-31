package com.hhplus.concert_ticketing.interfaces.filter;

import com.hhplus.concert_ticketing.interfaces.exception.ApiException;
import com.hhplus.concert_ticketing.interfaces.exception.ErrorCode;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class QueueFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 대기열 토큰 또는 상태 확인 (예시로 Authorization 헤더에서 토큰 추출)
        String queueToken = httpRequest.getHeader("Authorization");

        if (isQueueTokenValid(queueToken)) {
            log.info("Valid queue token: {}", queueToken);
            // 대기열 토큰이 유효한 경우, 필터 체인 다음으로 진행
            chain.doFilter(request, response);
        } else {
            // 대기열 토큰이 유효하지 않은 경우
            throw new ApiException(ErrorCode.QUEUE_TOKEN_INVALID_ERROR, LogLevel.WARN);
        }
    }


    // 대기열 토큰이 유효한지 체크하는 메서드
    private boolean isQueueTokenValid(String queueToken) {
        // 대기열 토큰 유효성 검사 로직
        // 토큰이 없거나, 유효하지 않은 경우 false 반환
        if (queueToken == null || queueToken.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {
        // 필터 종료 시 필요 작업
    }
}
