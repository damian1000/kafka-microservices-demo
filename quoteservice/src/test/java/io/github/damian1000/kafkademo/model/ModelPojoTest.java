package io.github.damian1000.kafkademo.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Plain-POJO round-trip tests. These models are wire types — exercising the
 * constructor and accessors makes sure a future refactor that adds validation
 * or renames a field doesn't silently break JSON binding.
 */
class ModelPojoTest {

    @Test
    void quoteRequestRoundTrip() {
        QuoteRequest req = new QuoteRequest(42L, "AAPL", BigDecimal.TEN);
        assertEquals(42L, req.getQuoteRequestId());
        assertEquals("AAPL", req.getSymbol());
        assertEquals(BigDecimal.TEN, req.getQuantity());
    }

    @Test
    void bestBidOfferRequestRoundTrip() {
        BestBidOfferRequest req = new BestBidOfferRequest("MSFT", BigDecimal.ONE);
        assertEquals("MSFT", req.getSymbol());
        assertEquals(BigDecimal.ONE, req.getQuantity());
        assertNotNull(new BestBidOfferRequest()); // no-arg ctor used by JSON binding
    }

    @Test
    void bestBidOfferAndQuoteAndCreateQuoteRequestNoArgConstructorsExist() {
        // JSON deserializers need these; we don't compare fields, just confirm
        // the empty constructor still exists so deserialization keeps working.
        assertNotNull(new BestBidOffer());
        assertNotNull(new Quote());
        assertNotNull(new CreateQuoteRequest());
    }
}
