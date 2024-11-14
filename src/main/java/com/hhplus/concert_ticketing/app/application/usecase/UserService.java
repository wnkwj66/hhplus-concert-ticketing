package com.hhplus.concert_ticketing.app.application.usecase;

import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.PointRepository;
import com.hhplus.concert_ticketing.app.domain.user.UserRepository;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository usersRepository;
    private final PointRepository pointRepository;

    // 유저 조회
    public Users getUserById(Long userId) {
        return usersRepository.findById(userId);
    }

    public Point getPointByUserId(Long userId) {
        Users user = getUserById(userId);
        return pointRepository.findById(user.getPointId());
    }

    public void deductPoint(Long userId, Integer totalAmount) {
        Point point = getPointByUserId(userId);
        point.deductAmount(totalAmount);
    }

    public Users createUser(String name) {
        Point point = new Point(0);
        pointRepository.save(point);

        // 유저 객체  생성
        Users user = new Users(point.getId(), name);

        return usersRepository.save(user);

    }
}
