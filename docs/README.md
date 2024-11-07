# Redis를 이용한 대기열 구현

### 1. 관계형 데이터베이스(RDB)로 대기열 구현
 - RDB는 데이터를 디스크에 저장하므로, 시스템 재시작 후에도 대기열 상태를 유지할 수 있어 데이터 손실을 방지하는데 유리하지만,  잦은 조회가 일어나거나 DB I/O 가 많은 조회에 대해 부하가 발생하게 되면 DB 성능저하를 초래할수 있습니다.
 - 영속성과 데이터 무결성을 중시하는 환경에서 적합합니다.
 - 성능이 중요한 환경에서는 병목 현상이 발생할 수 있으며, 확장성에 제한이 있을 수 있습니다.
### 2. Redis를 이용한 대기열 구현
 -  ### 고속 처리 
    - Redis는 인메모리 기반 데이터 저장소로, Disk 기반의 RDB 보다 빠른 성능을 제공합니다. 이는 잦은 조회가 발생하는 대기열 시스템에서 유리합니다.
-  ### 순서관리 
   - Redis는 기본적으로 FIFO 순서를 보장하는 큐 처리가 가능하여, 이를 통해서 대기열의 순차적 처리 요구를 만족합니다.
   - Sorted Set을 사용하면 Score를 사용해서 사용자들이 진입한 시각으로 우선순위를 결정할 수 있습니다.
- ### 지속성 및 내구성 옵션
  - 기본적으로 인메모리 구조이지만 , RDB나 AOF 옵션을 제공하여 메모리가 손실되더라도 복구가 가능합니다.

- ### 정리
  - 빠른 속도와 높은 처리량을 요구하는 시스템에 적합합니다.
  - 메모리 사용량과 데이터 손실 가능성, 복잡한 트랜잭션 처리의 부재 등을 고려해야 합니다.


## Redis를 이용한 대기열 구현 로직
```java
 /** 
 * 대기열 진입 로직
 * Sorted Set을 이용한 대기열 진입 로직 구현 
 * */ 

 @Transactional
    public Queue enterQueue(CreateQueueReq request) {
        Queue queue = new Queue(request.userId(), request.concertId(), request.performanceId(), Queue.generateJwtToken(request.userId()));
        String sortedSetKey = "queue:" + request.concertId() + ":" + request.performanceId();
        double score = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(sortedSetKey, queue.getToken(), score);
        // 토큰 만료 시간 설정 (5분)
        redisTemplate.expire(sortedSetKey, EXPIRED_TIME, TimeUnit.MINUTES);

        return queue;
    }
```
```java
/**
 * 대기열 정보 조회 로직
 * 대기열 정보를 조회하여 순서를 반환
 */
@Transactional
public QueueReulst checkQueue(String tokenId , SelectQueueReq request) {
    // Redis에서 대기열 객체를 먼저 조회
    String sortedSetKey = "queueSortedSet:" + request.concertId() + ":" + request.performanceId();
    // 사용자의 순위를 조회
    Long rank = redisTemplate.opsForZSet().rank(sortedSetKey, tokenId);
    log.info("[CreateRedisTokenUseCase] 현재 WorkingQueue 내의 사용자수 : " + rank);
    if (rank != null) {
        // 순위가 존재하면 활성 상태로 간주하고 순위 반환 (0부터 시작하므로 +1 해주기)
        QueueReulst reulst = new QueueReulst(new Queue(request.userId(), request.concertId(), request.performanceId(), tokenId), (rank+1));
        return reulst;
    } else {
        return new QueueReulst(null,null);
    }
}
```

```java
/**
 * 스케줄러를 이용하여 30초마다 50명의 유저를 활성화상태로 변경
 */
@Scheduled(fixedDelay = 30000)
public void processQueue() {
    queueUseCase.activateQueueForPerformances();
}

@Transactional
public void activateQueueForPerformances() {
    // 공연 목록을 가져와 각 공연별로 큐를 처리
    int availableSeat = 0;
    List<ConcertPerformance> performances = concertRepository.findByAvailableSeatGreaterThanOrStatusNot(availableSeat, ConcertStatus.SOLD_OUT);

    for (ConcertPerformance performance : performances) {
        Long concertId = performance.getConcertId();
        Long performanceId = performance.getId();
        String sortedSetKey = "queue:" + concertId + ":" + performanceId;  // 콘서트 및 공연별 대기열 키 생성
        // 대기열에서 앞에 있는 50명의 사용자 조회
        Set<String> activeUsers = redisTemplate.opsForZSet().range(sortedSetKey, 0, 49); // 50명 조회

        for (String tokenId : activeUsers) {
            // 사용자를 활성화 상태로 변경
            redisTemplate.opsForZSet().remove(sortedSetKey, tokenId);  // 원래 대기열에서 제거
            redisTemplate.opsForZSet().add("activeQueue:" + concertId + ":" + performanceId, tokenId, System.currentTimeMillis());  // 활성화된 대기열에 추가
        }
    }
}
```