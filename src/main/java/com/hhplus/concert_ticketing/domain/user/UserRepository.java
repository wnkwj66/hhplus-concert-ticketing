package com.hhplus.concert_ticketing.domain.user;

public interface UserRepository {
    Users findById(Long userId);
}
