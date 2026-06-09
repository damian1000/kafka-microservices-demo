package io.github.damian1000.kafkademo.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KafkaConsumerConfigTest {

    @Test
    void errorHandlerStopsContainerOnFirstFailedRecord() {
        CommonErrorHandler handler = KafkaConsumerConfig.loggingStopContainerErrorHandler();
        MessageListenerContainer container = mock(MessageListenerContainer.class);
        when(container.getListenerId()).thenReturn("listener-1");

        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, "k", "boom-payload");
        handler.handleRemaining(new RuntimeException("boom"), List.of(record), null, container);

        verify(container).stop();
    }

    @Test
    void errorHandlerWithNoRecordsIsNoOp() {
        CommonErrorHandler handler = KafkaConsumerConfig.loggingStopContainerErrorHandler();
        MessageListenerContainer container = mock(MessageListenerContainer.class);

        handler.handleRemaining(new RuntimeException("boom"), Collections.emptyList(), null, container);

        verify(container, never()).stop();
        verify(container, never()).getListenerId();
    }

    @Test
    void consumerFactoryBeansAreCreated() {
        KafkaConsumerConfig config = new KafkaConsumerConfig();
        ReflectionTestUtils.setField(config, "bootstrapServers", "localhost:9092");
        org.junit.jupiter.api.Assertions.assertNotNull(config.consumerFactory());
        org.junit.jupiter.api.Assertions.assertNotNull(config.myConsumerFactory());
        org.junit.jupiter.api.Assertions.assertNotNull(config.myConsumerFactoryForException());
    }
}
