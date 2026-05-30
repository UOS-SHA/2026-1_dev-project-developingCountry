package com.sha.backend.controller;

import com.sha.backend.domain.Order;
import com.sha.backend.domain.OrderSide;
import com.sha.backend.domain.OrderType;
import com.sha.backend.domain.Trade;
import com.sha.backend.dto.OrderRequest;
import com.sha.backend.service.TradingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final TradingService tradingService;

    public OrderController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping
    public Trade createOrder(@RequestBody OrderRequest dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setSymbol(dto.getSymbol());
        order.setType(OrderSide.valueOf(dto.getType()));
        order.setOrderType(OrderType.valueOf(dto.getOrderType()));
        order.setPrice(dto.getPrice());
        order.setQuantity(dto.getQuantity());

        return tradingService.executeOrder(order);
    }
}