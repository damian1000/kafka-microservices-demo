package io.github.damian1000.kafkademo.consumer;

import io.github.damian1000.kafkademo.service.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class KafkaConsumerTest {

    private EventPublisher eventPublisher;
    private KafkaConsumer consumer;

    @BeforeEach
    void setUp() {
        eventPublisher = Mockito.mock(EventPublisher.class);
        consumer = new KafkaConsumer();
        ReflectionTestUtils.setField(consumer, "service", eventPublisher);
    }

    @Test
    void childMessageGoesToKidsTopic() {
        String json = "{\"age\":5,\"message\":\"hello kids\"}";
        consumer.consume(json, "quickstart");
        verify(eventPublisher).sendDataToTopic("kidsTopic", "hello kids");
    }

    @Test
    void adultMessageGoesToAdultTopic() {
        String json = "{\"age\":30,\"message\":\"hello adults\"}";
        consumer.consume(json, "quickstart");
        verify(eventPublisher).sendDataToTopic("adultTopic", "hello adults");
    }

    @Test
    void boundaryAgeTenIsAdult() {
        String json = "{\"age\":10,\"message\":\"on the edge\"}";
        consumer.consume(json, "quickstart");
        // age < 10 -> kids; age >= 10 -> adults. 10 is the adult side.
        verify(eventPublisher).sendDataToTopic("adultTopic", "on the edge");
    }

    @Test
    void malformedJsonThrowsRuntime() {
        assertThrows(RuntimeException.class, () -> consumer.consume("not-json", "quickstart"));
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void dltHandlerDoesNotThrow() {
        consumer.dlt("any message", "any-topic");
    }
}
