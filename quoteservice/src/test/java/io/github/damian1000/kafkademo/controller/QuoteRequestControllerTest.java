package io.github.damian1000.kafkademo.controller;

import io.github.damian1000.kafkademo.model.BestBidOffer;
import io.github.damian1000.kafkademo.model.BestBidOfferRequest;
import io.github.damian1000.kafkademo.model.CreateQuoteRequest;
import io.github.damian1000.kafkademo.model.Quote;
import io.github.damian1000.kafkademo.service.BestBidOfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class QuoteRequestControllerTest {

    private BestBidOfferService bestBidOfferService;
    private QuoteRequestController controller;

    @BeforeEach
    void setUp() {
        bestBidOfferService = Mockito.mock(BestBidOfferService.class);
        controller = new QuoteRequestController();
        ReflectionTestUtils.setField(controller, "bestBidOfferService", bestBidOfferService);
    }

    @Test
    void createQuoteRequestForwardsToBboServiceAndWrapsResult() {
        when(bestBidOfferService.getBestBidOffer(any(BestBidOfferRequest.class)))
                .thenReturn(new BestBidOffer("AAPL", BigDecimal.TEN, BigDecimal.valueOf(20050), BigDecimal.valueOf(20100)));

        ResponseEntity<Quote> response = controller.createQuoteRequest(
                new CreateQuoteRequest("AAPL", BigDecimal.TEN));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Quote quote = response.getBody();
        assertNotNull(quote);
        assertEquals("AAPL", quote.getSymbol());
        assertEquals(BigDecimal.TEN, quote.getQuantity());
        assertEquals(BigDecimal.valueOf(20050), quote.getBid());
        assertEquals(BigDecimal.valueOf(20100), quote.getOffer());
    }
}
