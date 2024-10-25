package com.hhplus.concert_ticketing.infra.user;

import com.hhplus.concert_ticketing.domain.user.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface JpaPointRepository extends JpaRepository<Point,Long> {
    @Query("select p from Point p where p.userId = :userId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Point findByIdWithLock(Long userId);

}
