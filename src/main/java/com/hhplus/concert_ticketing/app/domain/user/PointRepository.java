package com.hhplus.concert_ticketing.app.domain.user;


public interface PointRepository {
    Point findById(Long userId);

    void save(Point point);

}
