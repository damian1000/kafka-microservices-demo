package io.github.damian1000.kafkademo.service;

import io.github.damian1000.kafkademo.model.BestBidOffer;
import io.github.damian1000.kafkademo.model.BestBidOfferRequest;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BestBidOfferService {

    private final RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

    @Value("${orderbook.base-url:http://localhost:8083}")
    private String orderBookBaseUrl;

    @SneakyThrows
    public BestBidOffer getBestBidOffer(BestBidOfferRequest bestBidOfferRequest) {
        HttpEntity<BestBidOfferRequest> request = new HttpEntity<>(bestBidOfferRequest);
        return restTemplate.postForObject(orderBookBaseUrl + "/order/bbo", request, BestBidOffer.class);
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(5000))
                        .build())
                .build();
        var httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setResponseTimeout(Timeout.ofMilliseconds(5000))
                        .build())
                .build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

}
