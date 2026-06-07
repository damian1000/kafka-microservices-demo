package io.github.damian1000.kafkademo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private EventPublisher eventPublisher;

    @Test
    void sendDataToTopicDelegatesToKafkaTemplate() {
        eventPublisher.sendDataToTopic("orders", "hello");
        verify(kafkaTemplate).send("orders", "hello");
    }
}
