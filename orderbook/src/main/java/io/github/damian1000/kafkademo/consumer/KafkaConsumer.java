package io.github.damian1000.kafkademo.consumer;

import io.github.damian1000.kafkademo.model.Content;
import io.github.damian1000.kafkademo.service.EventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @Autowired
    private EventPublisher service;

    @RetryableTopic(attempts = "3", backOff = @BackOff(delay = 5000, multiplier = 3.0))
    @KafkaListener(topics = "quickstart", groupId = "group_id", containerFactory = "myConsumerFactory")
    public void consume(String msg, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("received {} from {}", msg, topic);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Content topicData = mapper.readValue(msg, Content.class);
            if (topicData.getAge() < 10) {
                service.sendDataToTopic("kidsTopic", topicData.getMessage());
            } else {
                service.sendDataToTopic("adultTopic", topicData.getMessage());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DltHandler
    public void dlt(String msg, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.warn("dead message {} from {}", msg, topic);
    }

}
