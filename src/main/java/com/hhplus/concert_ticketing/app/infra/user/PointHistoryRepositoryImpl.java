package com.hhplus.concert_ticketing.app.infra.user;

import com.hhplus.concert_ticketing.app.domain.user.PointHistory;
import com.hhplus.concert_ticketing.app.domain.user.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {
    private final JpaPointHistoryRepository jpaPointHistoryRepository;
    @Override
    public void save(PointHistory pointHistory) {
        jpaPointHistoryRepository.save(pointHistory);
    }
}
