package com.hhplus.concert_ticketing.infra.user;

import com.hhplus.concert_ticketing.domain.user.Point;
import com.hhplus.concert_ticketing.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public Point findById(Long userId) {
        return jpaUserRepository.findById(userId);
    }

    @Override
    public Point findByIdWithOutLock(Long userId) {
        return jpaUserRepository.findByIdWithLock(userId);
    }
}
