package com.hhplus.concert_ticketing.domain.user;


public interface PointRepository {
    Point findById(Long userId);

    Point findByIdWithOutLock(Long userId);

    void save(Point point);

    Point findByIdWithLock(Long userId);
}
