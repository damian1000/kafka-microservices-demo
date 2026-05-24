package com.example.kafkademo.controller;

import com.example.kafkademo.model.BestBidOffer;
import com.example.kafkademo.model.BestBidOfferRequest;
import com.example.kafkademo.model.CreateQuoteRequest;
import com.example.kafkademo.model.Quote;
import com.example.kafkademo.service.BestBidOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/quote/request")
public class QuoteRequestController {

    @Autowired
    private BestBidOfferService bestBidOfferService;

    @PostMapping("/create")
    public ResponseEntity<Quote> createQuoteRequest(@RequestBody CreateQuoteRequest createQuoteRequest) {
        BestBidOffer bestBidOffer = bestBidOfferService.getBestBidOffer(new BestBidOfferRequest(
                createQuoteRequest.getSymbol(),
                createQuoteRequest.getQuantity()));
        Quote quote = new Quote(bestBidOffer.getSymbol(),
                bestBidOffer.getQuantity(),
                bestBidOffer.getBid(),
                bestBidOffer.getOffer());
        return status(HttpStatus.CREATED).body(quote);
    }
}
