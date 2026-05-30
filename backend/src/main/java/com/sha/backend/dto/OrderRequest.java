package com.sha.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequest {
    private Long userId;
    private String symbol;
    private String type;       // BUY, SELL
    private String orderType;  // LIMIT, MARKET
    private Double price;
    private Double quantity;
}
