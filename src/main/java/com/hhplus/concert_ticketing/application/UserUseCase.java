package com.hhplus.concert_ticketing.application;

import com.hhplus.concert_ticketing.domain.user.*;
import com.hhplus.concert_ticketing.interfaces.exception.ApiException;
import io.jsonwebtoken.Claims;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.concert_ticketing.interfaces.exception.ErrorCode.BAD_REQUEST_ERROR;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public Integer selectUserAmount(String token) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = pointRepository.findByIdWithOutLock(userId);
        return userPoint.getAmount();

    }
    @Transactional
    public Integer chargeUserAmount(String token, Integer amount) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = pointRepository.findById(userId);
        userPoint.addAmount(amount);

        // @Transactional 이 적용된 메서드안에서 JPA 엔티티를 값을 변경하면,
        // 트랜잭션이 끝날 때 JPA 가 자동으로 변경된 값을 반영해 DB에 업데이트
        // pointRepository.save(userPoint);

        // 포인트 충전 기록 추가
        PointHistory pointHistory = PointHistory.enterPointHitory(userId, amount, PointType.CHARGE);
        pointHistoryRepository.save(pointHistory);

        return userPoint.getAmount();
    }

    @Transactional
    @Retryable(value = OptimisticLockException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 200))
    public Integer chargeUserAmountOptimisticLock(String token, Integer amount) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);
        // 낙관적 락이 걸린 상태로 데이터 조회 및 수정
        Point userPoint = pointRepository.findById(userId);
        userPoint.addAmount(amount);

        PointHistory pointHistory = PointHistory.enterPointHitory(userId, amount, PointType.CHARGE);
        pointHistoryRepository.save(pointHistory);

        return userPoint.getAmount();

    }

}
