package com.hhplus.concert_ticketing.infra.concert;

import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.domain.concert.Performance;
import com.hhplus.concert_ticketing.domain.concert.Seat;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ConcertJPARepository implements ConcertRepository{
    private final EntityManager em;

    public ConcertJPARepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Concert concertReqInfo) {
        em.persist(concertReqInfo);
    }

    @Override
    public Seat save(Seat seat) {
        em.persist(seat);
        return seat;
    }

    @Override
    public List<Concert> getConcertList() {
        return em.createQuery("select t from Concert as t", Concert.class)
                .getResultList();
    }

    @Override
    public Optional<Concert> findById(Long concertId) {
        Concert concert = em.find(Concert.class,concertId);
        return Optional.ofNullable(concert);
    }

    @Override
    public List<Performance> getPerformanceList(Long concertId) {
        List<Performance> result = em.createQuery("select t from Performance as t where t.concertId = :concertId", Performance.class)
                .setParameter("concertId", concertId)
                .getResultList();

        return result;
    }

    @Override
    public List<Seat> getAvailableSeatList(Long performanceId) {
        List<Seat> result = em.createQuery("select t from Seat as t where t.performanceId = :performanceId and t.status = AVAILABLE", Seat.class)
                .setParameter("performanceId", performanceId)
                .getResultList();
        return result;
    }

    @Override
    public Optional<Seat> getSeatById(Long seatId) {
        Seat seat = em.find(Seat.class,seatId);
        return Optional.ofNullable(seat);
    }
}
