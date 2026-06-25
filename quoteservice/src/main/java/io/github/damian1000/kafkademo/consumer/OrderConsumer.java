package io.github.damian1000.kafkademo.consumer;

import io.github.damian1000.kafkademo.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final KafkaService service;

    @RetryableTopic(attempts = "3", backOff = @BackOff(delay = 5000, multiplier = 3.0))
    @KafkaListener(topics = "order", groupId = "group_id", containerFactory = "myConsumerFactory")
    public void consume(String msg, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("received {} from {}", msg, topic);
        if (msg.contains("DLT")) {
            throw new IllegalStateException("Intentional failure for retry/DLT demo");
        }
    }

    @DltHandler
    public void dlt(String msg, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.warn("dead message {} from {}", msg, topic);
    }

}
