package com.hhplus.concert_ticketing.app.infra.repository.user;

import com.hhplus.concert_ticketing.app.domain.user.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistory,Long> {
}
