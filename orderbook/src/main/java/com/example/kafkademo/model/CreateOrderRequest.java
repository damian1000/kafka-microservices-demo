package com.example.kafkademo.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    private String symbol;
    private BigDecimal quantity;
    private String side;
    private BigDecimal price;
}
