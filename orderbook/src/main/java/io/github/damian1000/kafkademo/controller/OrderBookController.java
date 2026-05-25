package io.github.damian1000.kafkademo.controller;

import io.github.damian1000.kafkademo.model.BestBidOffer;
import io.github.damian1000.kafkademo.model.BestBidOfferRequest;
import io.github.damian1000.kafkademo.model.CreateOrderRequest;
import io.github.damian1000.kafkademo.service.EventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Random;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/order")
public class OrderBookController {

    private EventPublisher eventPublisher;

    public OrderBookController(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/bbo")
    public ResponseEntity<BestBidOffer> getBestBidOffer(@RequestBody BestBidOfferRequest bestBidOfferRequest) {
        BestBidOffer bestBidOffer = new BestBidOffer(
                bestBidOfferRequest.getSymbol(),
                bestBidOfferRequest.getQuantity(),
                BigDecimal.valueOf(20050),
                BigDecimal.valueOf(20100)
        );

        return status(HttpStatus.CREATED).body(bestBidOffer);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        String event = "DLT".equalsIgnoreCase(createOrderRequest.getSymbol())
                ? "createOrderEvent-fail"
                : "createOrderEvent";
        eventPublisher.sendDataToTopic("order", event);
        return status(HttpStatus.CREATED).body(new Random().nextLong());
    }

}
