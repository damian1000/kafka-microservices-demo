package io.github.damian1000.kafkademo.consumer;

import io.github.damian1000.kafkademo.service.KafkaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

class OrderConsumerTest {

    private KafkaService kafkaService;
    private OrderConsumer consumer;

    @BeforeEach
    void setUp() {
        kafkaService = Mockito.mock(KafkaService.class);
        consumer = new OrderConsumer();
        ReflectionTestUtils.setField(consumer, "service", kafkaService);
    }

    @Test
    void messageWithoutFailDoesNothingExtra() {
        consumer.consume("createOrderEvent", "order");
        verifyNoInteractions(kafkaService);
    }

    @Test
    void messageContainingFailThrowsToTriggerRetryAndDlt() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> consumer.consume("createOrderEvent-fail", "order"));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("Intentional failure"));
    }

    @Test
    void dltHandlerDoesNotThrow() {
        consumer.dlt("any", "any-topic");
    }
}
