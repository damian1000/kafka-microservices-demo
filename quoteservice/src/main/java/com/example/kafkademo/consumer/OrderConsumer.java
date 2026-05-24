package com.example.kafkademo.consumer;

import com.example.kafkademo.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    @Autowired
    private KafkaService service;

    @RetryableTopic(attempts = "3", backOff = @BackOff(delay = 5000, multiplier = 3.0))
    @KafkaListener(topics = "order", groupId = "group_id", containerFactory = "myConsumerFactory")
    public void consume(String msg, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println(msg +" from "+topic);
        if (msg.contains("fail")) {
            throw new IllegalStateException("Intentional failure for retry/DLT demo");
        }
    }

    @DltHandler
    public void dlt(String msg, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        System.out.println("Dead Message : "+msg +" from "+topic);
    }

}
