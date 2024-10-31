package com.hhplus.concert_ticketing.infra.repository.user;

import com.hhplus.concert_ticketing.domain.user.PointHistory;
import com.hhplus.concert_ticketing.domain.user.PointHistoryRepository;
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
