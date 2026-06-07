package io.github.damian1000.kafkademo.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaContainerControllerTest {

    private final KafkaContainerController controller = new KafkaContainerController();

    @Test
    void startEndpointReturnsStartedMessage() {
        assertEquals("kafka consumer topic started", controller.startKafkaConsumer("QSE-01"));
    }

    @Test
    void stopEndpointReturnsStoppedMessage() {
        assertEquals("kafka consumer topic stopped", controller.stopKafkaConsumer("QSE-01"));
    }
}
