package com.example.kafkademo.model;

import java.math.BigDecimal;

public class BestBidOfferRequest {
    private String symbol;
    private BigDecimal quantity;

    public BestBidOfferRequest() {
    }

    public BestBidOfferRequest(String symbol, BigDecimal quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}
