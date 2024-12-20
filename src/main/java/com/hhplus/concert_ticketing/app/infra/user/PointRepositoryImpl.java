package com.hhplus.concert_ticketing.app.infra.user;

import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
    private final JpaPointRepository jpaPointRepository;

    @Override
    public Point findById(Long userId) {
        return jpaPointRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("유저 포인트 정보를 찾을 수 없습니다."));
    }

    @Override
    public void save(Point point) {
        jpaPointRepository.save(point);
    }


}
