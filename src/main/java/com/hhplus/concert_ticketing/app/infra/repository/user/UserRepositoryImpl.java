package com.hhplus.concert_ticketing.app.infra.repository.user;

import com.hhplus.concert_ticketing.app.domain.user.UserRepository;
import com.hhplus.concert_ticketing.app.domain.user.Users;
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
