package com.hhplus.concert_ticketing.app.application;

import com.hhplus.concert_ticketing.app.domain.user.*;
import com.hhplus.concert_ticketing.domain.user.*;
import io.jsonwebtoken.Claims;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private static final Logger logger = LoggerFactory.getLogger(UserUseCase.class);

    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public Integer selectUserAmount(String token) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = pointRepository.findByIdWithLock(userId);
        return userPoint.getAmount();

    }
    @Transactional
    public Integer chargeUserAmount(String token, Integer amount) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = pointRepository.findById(userId);
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

        Point userPoint = pointRepository.findById(userId);
        userPoint.addAmount(amount);

        // 포인트 충전 기록 추가
        PointHistory pointHistory = PointHistory.enterPointHitory(userId, amount, PointType.CHARGE);
        pointHistoryRepository.save(pointHistory);

        return userPoint.getAmount();
    }

    @Transactional
    public Integer chargeUserAmountPessimisticLock(String token, Integer amount) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = pointRepository.findByIdWithLock(userId);
        userPoint.addAmount(amount);

        // 포인트 충전 기록 추가
        PointHistory pointHistory = PointHistory.enterPointHitory(userId, amount, PointType.CHARGE);
        pointHistoryRepository.save(pointHistory);

        return userPoint.getAmount();
    }

    @Transactional
    public Integer chargeUserAmountRedisLock(String token, Integer amount) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);
        boolean lockAcquired = false;

        // Redis 분산 락 설정
        RLock lock = redissonClient.getLock("pointLock:" + userId);

        try {
            // 최대 5초 대기후 3초동안 락 유지
            lockAcquired = lock.tryLock(10,5, TimeUnit.SECONDS);
            if (lockAcquired) {
                logger.info("락 획득 성공: userId={}, amount={}", userId, amount);

                Point userPoint = pointRepository.findById(userId);
                logger.info("충전 전 포인트 상태: userId={}, currentAmount={}", userId, userPoint.getAmount());
                userPoint.addAmount(amount);
                pointRepository.save(userPoint);

                // 포인트 충전 기록 추가
                PointHistory pointHistory = PointHistory.enterPointHitory(userId, amount, PointType.CHARGE);
                pointHistoryRepository.save(pointHistory);

                logger.info("충전 후 포인트 상태: userId={}, updatedAmount={}", userId, userPoint.getAmount());
                return userPoint.getAmount();
            } else {
                logger.warn("락 획득 실패: userId={}, amount={}", userId, amount);
                throw new RuntimeException("포인트 충전 중에 다른 요청이 처리되었습니다.");
            }
        } catch (InterruptedException e) {
            logger.error("락 대기 중 인터럽트 발생: userId={}, amount={}", userId, amount, e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Redis 락 획득 중 오류 발생", e);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                logger.info("락 해제 완료: userId={}", userId);
            }
        }
    }
}
