package com.hhplus.concert_ticketing.application.queue;

import com.hhplus.concert_ticketing.api.queue.dto.TokenReqDto;
import com.hhplus.concert_ticketing.domain.queue.QueueToken;
import com.hhplus.concert_ticketing.infra.queue.QueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateQueueService implements QueueService{

    private final QueueRepository queueRepository;

    public CreateQueueService(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    @Override
    @Transactional
    public com.hhplus.concert_ticketing.domain.queue.QueueToken getOrCreateToken(TokenReqDto request) {
        // 기존 토큰이 있는지 확인
        Optional<QueueToken> existingToken = queueRepository.findByUserIdAndConcertId(request.userId(), request.concertId());

        if (existingToken.isPresent()) {
            return existingToken.get(); // 이미 존재하는 토큰 반환
        }
        int position = queueRepository.countByConcertId(request.concertId()) + 1;
        String tokenId = UUID.randomUUID().toString();
        UUID.randomUUID().toString();
        // 새로운 토큰 생성
        QueueToken newToken = new QueueToken(
                request.userId(),
                request.concertId(),
                UUID.randomUUID().toString(),  // 토큰 ID 생성
                "WAIT",                        // 상태
                position,                      // 대기 순서
                LocalDateTime.now(),           // 생성 시각
                LocalDateTime.now(),           // 업데이트 시각
                LocalDateTime.now().plusMinutes(30) // 만료 시각
        );


        return queueRepository.save(newToken);
    }
}
