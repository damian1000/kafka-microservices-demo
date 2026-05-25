package io.github.damian1000.kafkademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendDataToTopic(String topic, String msg) {
        kafkaTemplate.send(topic, msg);
    }
}
