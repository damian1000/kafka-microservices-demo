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
    void bestBidOfferRequestRoundTrip() {
        BestBidOfferRequest req = new BestBidOfferRequest("MSFT", BigDecimal.ONE);
        assertEquals("MSFT", req.getSymbol());
        assertEquals(BigDecimal.ONE, req.getQuantity());
        assertNotNull(new BestBidOfferRequest()); // no-arg ctor used by JSON binding
    }
}
