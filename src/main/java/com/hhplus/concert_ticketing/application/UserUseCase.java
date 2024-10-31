package com.hhplus.concert_ticketing.application;

import com.hhplus.concert_ticketing.domain.user.*;
import io.jsonwebtoken.Claims;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public Integer selectUserAmount(String token) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = userRepository.findByIdWithOutLock(userId);
        return userPoint.getAmount();

    }
    @Transactional
    public Integer chargeUserAmount(String token, Integer amount) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = userRepository.findById(userId);
        userPoint.addAmount(amount);

        PointHistory pointHistory = PointHistory.enterPointHitory(userId, amount, PointType.CHARGE);
        pointHistoryRepository.save(pointHistory);

        return userPoint.getAmount();
    }


    @Transactional
    @Retryable(value = OptimisticLockException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 100))
    public Integer chargeUserAmountOptimisticLock(String token, Integer amount) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = userRepository.findById(userId);
        userPoint.addAmount(amount);

        // 포인트 충전 기록 추가
        PointHistory pointHistory = PointHistory.enterPointHitory(userId, amount, PointType.CHARGE);
        pointHistoryRepository.save(pointHistory);

        return userPoint.getAmount();
    }
}
