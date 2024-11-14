package com.hhplus.concert_ticketing.app.infra.user;

import com.hhplus.concert_ticketing.app.domain.user.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistory,Long> {
}
