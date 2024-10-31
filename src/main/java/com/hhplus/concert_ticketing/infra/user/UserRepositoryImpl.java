package com.hhplus.concert_ticketing.infra.user;

import com.hhplus.concert_ticketing.domain.user.Point;
import com.hhplus.concert_ticketing.domain.user.UserRepository;
import com.hhplus.concert_ticketing.domain.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUsersRepository jpaUserRepository;


    @Override
    public Users findById(Long userId) {
        return jpaUserRepository.findById(userId).orElse(null);
    }
}
