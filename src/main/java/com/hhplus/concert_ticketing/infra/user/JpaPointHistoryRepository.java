package com.hhplus.concert_ticketing.infra.user;

import com.hhplus.concert_ticketing.domain.user.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistory,Long> {
}
