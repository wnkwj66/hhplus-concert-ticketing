package com.hhplus.concert_ticketing.app.domain.user;

public interface UserRepository {
    Users findById(Long userId);

    Users save(Users user);
}
