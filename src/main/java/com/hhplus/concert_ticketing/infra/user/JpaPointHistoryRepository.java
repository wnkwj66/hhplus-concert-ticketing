package com.hhplus.concert_ticketing.infra.user;

import com.hhplus.concert_ticketing.domain.user.PointHistory;

public interface JpaPointHistoryRepository {
    void save(PointHistory pointHistory);
}
