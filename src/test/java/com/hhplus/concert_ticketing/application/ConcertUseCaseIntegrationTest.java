package com.hhplus.concert_ticketing.application;

import com.hhplus.concert_ticketing.domain.concert.*;
import com.hhplus.concert_ticketing.domain.payment.Payment;
import com.hhplus.concert_ticketing.domain.queue.Queue;
import com.hhplus.concert_ticketing.domain.user.Point;
import com.hhplus.concert_ticketing.domain.user.Users;
import com.hhplus.concert_ticketing.infra.concert.JpaConcertRepository;
import com.hhplus.concert_ticketing.infra.concert.JpaPerformanceRepository;
import com.hhplus.concert_ticketing.infra.concert.JpaSeatRepository;
import com.hhplus.concert_ticketing.infra.queue.JpaQueueRepository;
import com.hhplus.concert_ticketing.infra.user.JpaPointRepository;
import com.hhplus.concert_ticketing.infra.user.JpaUsersRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ConcertUseCaseIntegrationTest {
    @Autowired
    private ConcertUseCase concertUseCase;

    @Autowired
    private JpaConcertRepository jpaConcertRepository;
    @Autowired
    private JpaPerformanceRepository jpaPerformanceRepository;
    @Autowired
    private JpaSeatRepository jpaSeatRepository;
    @Autowired
    private JpaQueueRepository jpaQueueRepository;

    @Autowired
    private JpaUsersRepository jpaUserRepository;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    private String token;
    private Long userId;
    private Queue queue;

    @BeforeEach
    public void setUp() {
        // JWT 토큰 생성 (예: userId = 1)
        userId = 1L;
        token = Jwts.builder()
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, "secretKey")
                .compact();


        // 필요한 테스트 데이터 생성
        Concert concert = jpaConcertRepository.save(new Concert(1L, "Concert A", LocalDateTime.of(2024, 10, 3, 12, 0, 0), LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 11)));

        ConcertPerformance performance = jpaPerformanceRepository.save(new ConcertPerformance(1L, concert.getId(), ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50));

        Seat seat = jpaSeatRepository.save(new Seat(15L,performance.getId(), 15, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusMinutes(5)));


        // 테스트용 Queue 생성
        queue = new Queue(userId, concert.getId(),performance.getId(),token);
        jpaQueueRepository.save(queue);
    }
    @AfterEach
    void cleanUp() {
        jpaSeatRepository.deleteAll();
        jpaPerformanceRepository.deleteAll();
        jpaConcertRepository.deleteAll();
    }

    @Test
    void 콘서트_조회_기능() {
        List<Concert> concertList = concertUseCase.selectConcertList();

        assertEquals(1, concertList.size());
        assertEquals("Concert A", concertList.get(0).getTitle());
    }

    @Test
    void 콘서트_회차_조회_기능() {
        List<ConcertPerformance> performancesList = concertUseCase.selectPerformances(1L);

        assertEquals(1, performancesList.size());
    }

    @Test
    void 예매가능한_좌석조회_기능() {
        List<Seat> seatList = concertUseCase.selectSeats(1L);
        assertEquals(1, seatList.size());
    }

    @Test
    @DirtiesContext
    void 예약_성공_테스트() {
        Reservation reservation = concertUseCase.reserveConcert(token, 1L,15L);

        assertEquals(15L, reservation.getSeatId());
        assertEquals(ReservationStatus.TEMPORARY, reservation.getStatus());
    }

    @Test
    @DirtiesContext
    void 결제_성공_테스트() {
        // given
        Users user = jpaUserRepository.save(new Users(1L,1L,"테스터"));
        Point userPoint = new Point(1L, 10000);
        jpaPointRepository.save(userPoint);

        // when
        Payment payment = concertUseCase.paymentReservation(token, 1L);

        // then
        assertEquals(50000,payment.getAmount());
    }
}