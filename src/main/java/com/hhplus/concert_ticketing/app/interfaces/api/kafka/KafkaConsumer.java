package com.hhplus.concert_ticketing.app.interfaces.api.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "payment-topic",groupId = "concert-ticketing-group")
    public void receive(ConsumerRecord<String, String> consumerRecord) {
        try {
            log.info("수신한 키: {}", consumerRecord.key());
            log.info("수신한 메세지: {}", consumerRecord.value());
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
