package com.hhplus.concert_ticketing.application.concert;

import com.hhplus.concert_ticketing.api.concert.dto.ConcertInfo;
import com.hhplus.concert_ticketing.api.concert.dto.PerformanceResDto;
import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.domain.concert.Performance;
import com.hhplus.concert_ticketing.domain.concert.Seat;
import com.hhplus.concert_ticketing.domain.queue.QueueToken;
import com.hhplus.concert_ticketing.infra.concert.ConcertRepository;
import com.hhplus.concert_ticketing.infra.queue.QueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailableConcert implements ConcertService{
    private final ConcertRepository concertRepository;
    private final QueueRepository queueRepository;

    public AvailableConcert(ConcertRepository concertRepository, QueueRepository queueRepository) {
        this.concertRepository = concertRepository;
        this.queueRepository = queueRepository;
    }


    @Override
    public List<ConcertInfo> findConcertList() {
        List<Concert> concerts = concertRepository.getConcertList();

        List<ConcertInfo> concertInfos = concerts.stream()
                .map(concert -> new ConcertInfo(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getStatus(),
                        concert.getReservationStartAt(),
                        concert.getStartDate(),
                        concert.getEndDate()
                ))
                .collect(Collectors.toList());
        return concertInfos;
    }

    @Override
    public PerformanceResDto findPerformances(Long concertId) {
        //Optional<Concert> concert = concertRepository.findById(concertId);
        Optional<Concert> concert = concertRepository.findById(concertId);
        List<Performance> performances = concertRepository.getPerformanceList(concertId);

        PerformanceResDto performanceInfo = new PerformanceResDto(
                concert.get().getId(),
                concert.get().getTitle(),
                concert.get().getStatus(),
                performances
        );
        return performanceInfo;
    }

    @Override
    @Transactional
    public void save(Concert concertReqInfo) {
        concertRepository.save(concertReqInfo);
    }

    @Override
    public List<Seat> getAvailableSeatList(Long concertId, Long performanceId) {
        Optional<Concert> concert = concertRepository.findById(concertId);
        if(concert.get().getReservationStartAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("예약 가능한 시간이 아닙니다");
        }

        return concertRepository.getAvailableSeatList(performanceId);
    }


    @Override
    public Seat requestSeat(String uuid, Long seatId) {
        Optional<QueueToken> queueToken = queueRepository.findByUuid(uuid);

        //TODO : 검증

        Optional<Seat> seat = concertRepository.getSeatById(seatId);

        if(!seat.get().getStatus().equals("AVAILABLE")) {
            //TODO: 예외
        }
        // TODO: 좌석 상태 변경, 예약 업데이트(임시예약) 같이 진행
        // 좌석 상태 업데이트
        Seat updateSeat = new Seat(seatId,"TEMP");
        return concertRepository.save(updateSeat);

    }
}
