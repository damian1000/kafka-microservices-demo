package io.github.damian1000.kafkademo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaService kafkaService;

    @Test
    void sendDataToTopicDelegatesToKafkaTemplate() {
        kafkaService.sendDataToTopic("quotes", "payload");
        verify(kafkaTemplate).send("quotes", "payload");
    }
}
