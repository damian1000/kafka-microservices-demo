package io.github.damian1000.kafkademo.controller;

import io.github.damian1000.kafkademo.model.BestBidOffer;
import io.github.damian1000.kafkademo.model.BestBidOfferRequest;
import io.github.damian1000.kafkademo.model.CreateOrderRequest;
import io.github.damian1000.kafkademo.service.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class OrderBookControllerTest {

    private EventPublisher eventPublisher;
    private OrderBookController controller;

    @BeforeEach
    void setUp() {
        eventPublisher = mock(EventPublisher.class);
        controller = new OrderBookController(eventPublisher);
    }

    @Test
    void getBestBidOfferReturnsCreatedWithStubbedQuote() {
        BestBidOfferRequest req = new BestBidOfferRequest("AAPL", BigDecimal.TEN);

        ResponseEntity<BestBidOffer> response = controller.getBestBidOffer(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BestBidOffer body = response.getBody();
        assertNotNull(body);
        assertEquals("AAPL", body.getSymbol());
        assertEquals(BigDecimal.TEN, body.getQuantity());
        assertEquals(BigDecimal.valueOf(20050), body.getBid());
        assertEquals(BigDecimal.valueOf(20100), body.getOffer());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void createOrderPublishesNormalEventForRegularSymbol() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setSymbol("MSFT");
        req.setQuantity(BigDecimal.valueOf(5));
        ResponseEntity<Long> response = controller.createOrder(req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(eventPublisher).sendDataToTopic("order", "createOrderEvent");
    }

    @Test
    void createOrderWithDltSymbolPublishesFailEvent() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setSymbol("DLT");
        req.setQuantity(BigDecimal.ONE);
        controller.createOrder(req);
        verify(eventPublisher).sendDataToTopic("order", "createOrderEvent-fail");
    }

    @Test
    void dltMatchIsCaseInsensitive() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setSymbol("dlt");
        req.setQuantity(BigDecimal.ONE);
        controller.createOrder(req);
        verify(eventPublisher).sendDataToTopic("order", "createOrderEvent-fail");
    }
}
