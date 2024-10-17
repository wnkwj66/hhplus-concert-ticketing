package com.hhplus.concert_ticketing.api.queue.controller;

import com.hhplus.concert_ticketing.api.queue.dto.TokenReqDto;
import com.hhplus.concert_ticketing.api.queue.dto.TokenResDto;
import com.hhplus.concert_ticketing.application.queue.QueueService;
import com.hhplus.concert_ticketing.domain.queue.QueueToken;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class QueueController {
    // 물건, 제도 , 철학
    // 철학을 보는 사람이 되자.
    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    /**
     * 토큰 조회 및 생성
     * @param request
     * @return
     */
    @Operation(summary = "대기열 토큰 발급 요청 API", description = "토큰을 조회하고 발급받습니다.")
    @PostMapping("/tokens")
    public ResponseEntity<TokenResDto> getToken(@RequestBody TokenReqDto request) {
        System.out.println("토큰 발급 요청");
        QueueToken token = queueService.getOrCreateToken(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResDto(
                token.getId(),
                token.getUserId(),
                token.getConcertId(),
                token.getTokenUUID(),
                token.getStatus(),
                token.getPosition(),
                token.getCreateAt(),
                token.getExpiredAt()
        ));
    }

}


/*


@Repository
class JpaQueueRepository implements QueueRepository {
    private final EntityManager em;

    JpaQueueRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<QueueToken> findByUserIdAndConcertId(Long userId, Long concertId) {
        return Optional.empty();
    }

    @Override
    public int countByConcertId(Long concertId) {
        String jpql = "SELECT COUNT(q) FROM QueueToken q WHERE q.concertId = :concertId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("concertId", concertId);
        return query.getSingleResult().intValue();
    }

    @Override
    public QueueToken save(QueueToken token) {
        em.persist(token);
        return token;
    }
}
*/



