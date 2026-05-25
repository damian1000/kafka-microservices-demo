package io.github.damian1000.kafkademo.model;

import java.math.BigDecimal;

public class BestBidOffer {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal bid;
    private BigDecimal offer;

    public BestBidOffer() {
    }

    public BestBidOffer(String symbol, BigDecimal quantity, BigDecimal bid, BigDecimal offer) {
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
