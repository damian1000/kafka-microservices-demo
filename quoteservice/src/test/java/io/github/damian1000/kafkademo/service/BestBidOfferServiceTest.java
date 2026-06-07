package io.github.damian1000.kafkademo.service;

import io.github.damian1000.kafkademo.model.BestBidOffer;
import io.github.damian1000.kafkademo.model.BestBidOfferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BestBidOfferServiceTest {

    private RestTemplate restTemplate;
    private BestBidOfferService service;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        service = new BestBidOfferService();
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(service, "orderBookBaseUrl", "http://orderbook.test");
    }

    @Test
    void postsRequestToConfiguredBaseUrlAndReturnsBody() {
        BestBidOffer canned = new BestBidOffer("AAPL", BigDecimal.TEN, BigDecimal.valueOf(200), BigDecimal.valueOf(201));
        when(restTemplate.postForObject(eq("http://orderbook.test/order/bbo"), any(HttpEntity.class), eq(BestBidOffer.class)))
                .thenReturn(canned);

        BestBidOffer result = service.getBestBidOffer(new BestBidOfferRequest("AAPL", BigDecimal.TEN));

        assertEquals(canned, result);
    }

    @Test
    void wrapsRequestBodyAsHttpEntity() {
        ArgumentCaptor<HttpEntity<BestBidOfferRequest>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplate.postForObject(any(String.class), captor.capture(), eq(BestBidOffer.class)))
                .thenReturn(new BestBidOffer("AAPL", BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE));

        BestBidOfferRequest req = new BestBidOfferRequest("AAPL", BigDecimal.ONE);
        service.getBestBidOffer(req);

        assertEquals(req, captor.getValue().getBody());
    }
}
