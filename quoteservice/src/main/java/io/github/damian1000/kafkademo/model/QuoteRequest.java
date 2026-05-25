package io.github.damian1000.kafkademo.model;

import java.math.BigDecimal;

public class QuoteRequest {

    private Long quoteRequestId;

    private String symbol;

    private BigDecimal quantity;

    public QuoteRequest(Long quoteRequestId, String symbol, BigDecimal quantity) {
        this.quoteRequestId = quoteRequestId;
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public Long getQuoteRequestId() {
        return quoteRequestId;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}
