package com.hhplus.concert_ticketing.app.infra.kafka;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleProducerTest {
    @Autowired
    private SimpleProducer simpleProducer;

    @Test
    @DisplayName("메세지_전송")
    void 메세지_전송() throws Exception {
        simpleProducer.sendMessage("dev-topic", "hello world");
        simpleProducer.sendMessage("dev-topic", "key1", "hello world");
        simpleProducer.sendMessage("dev-topic", 0, "key1", "hello world");
    }
}