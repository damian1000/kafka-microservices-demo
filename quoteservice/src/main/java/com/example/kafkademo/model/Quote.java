package com.example.kafkademo.model;

import java.math.BigDecimal;

public class Quote {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal bid;
    private BigDecimal offer;

    public Quote() {
    }

    public Quote(String symbol, BigDecimal quantity, BigDecimal bid, BigDecimal offer) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.bid = bid;
        this.offer = offer;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getOffer() {
        return offer;
    }
}
