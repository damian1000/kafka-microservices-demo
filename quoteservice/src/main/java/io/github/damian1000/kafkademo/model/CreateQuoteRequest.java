package io.github.damian1000.kafkademo.model;

import java.math.BigDecimal;

public class CreateQuoteRequest {
    private String symbol;
    private BigDecimal quantity;

    public CreateQuoteRequest() {
    }

    public CreateQuoteRequest(String symbol, BigDecimal quantity) {
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
