package com.hhplus.concert_ticketing.domain.user;

public interface UserRepository {
    Point findById(Long userId);

    Point findByIdWithOutLock(Long userId);
}
