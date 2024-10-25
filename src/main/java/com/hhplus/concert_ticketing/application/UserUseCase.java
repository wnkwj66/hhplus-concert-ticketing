package com.hhplus.concert_ticketing.application;

import com.hhplus.concert_ticketing.domain.user.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public Integer selectUserAmount(String token) {
        Claims claims = User.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = userRepository.findByIdWithOutLock(userId);
        return userPoint.getAmount();

    }
    @Transactional
    public Integer chargeUserAmount(String token, Integer amount) {
        Claims claims = User.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Point userPoint = userRepository.findById(userId);
        userPoint.addAmount(amount);

        PointHistory pointHistory = PointHistory.enterPointHitory(userId, amount, PointType.CHARGE);
        pointHistoryRepository.save(pointHistory);

        return userPoint.getAmount();
    }
}
