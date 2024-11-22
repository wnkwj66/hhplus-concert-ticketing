package com.hhplus.concert_ticketing.app.infra.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class SimpleProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        log.info("topic :" +topic +"  send message : "+ message);
        ProducerRecord record = new ProducerRecord(topic, message);
        kafkaTemplate.send(record);
    }

    public void sendMessage(String topic, String key, String message) {
        log.info("topic :" +topic +"||  Key : "+ key +"||  send message : "+ message);
        ProducerRecord record = new ProducerRecord(topic, key, message);
        kafkaTemplate.send(record);
    }

    public void sendMessage(String topic, int partition, String key, String message) {
        ProducerRecord record = new ProducerRecord(topic, partition, key, message);
        kafkaTemplate.send(record);
    }
}
