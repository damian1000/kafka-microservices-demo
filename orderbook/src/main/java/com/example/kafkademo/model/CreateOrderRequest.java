package com.example.kafkademo.model;

import java.math.BigDecimal;

public class CreateOrderRequest {

    private String symbol;
    private BigDecimal quantity;
    private String side;
    private BigDecimal price;
}
