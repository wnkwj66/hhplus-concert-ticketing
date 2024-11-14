package com.hhplus.concert_ticketing.app.infra.user;

import com.hhplus.concert_ticketing.app.domain.user.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaPointRepository extends JpaRepository<Point,Long> {

}
