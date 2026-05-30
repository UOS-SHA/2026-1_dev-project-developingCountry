package com.sha.backend.service;

import com.sha.backend.domain.Order;
import com.sha.backend.domain.OrderStatus;
import com.sha.backend.domain.Trade;
import com.sha.backend.domain.Holding;
import com.sha.backend.repository.OrderRepository;
import com.sha.backend.repository.TradeRepository;
import com.sha.backend.repository.HoldingRepository;
import com.sha.backend.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradingService {

    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;
    private final HoldingRepository holdingRepository;
    private final UserRepository userRepository;

    public TradingService(OrderRepository orderRepository, TradeRepository tradeRepository,
                          HoldingRepository holdingRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
        this.holdingRepository = holdingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Trade executeOrder(Order order) {
        if (order.getOrderType() == com.sha.backend.domain.OrderType.MARKET) {
            return executeMarketOrder(order);
        } else {
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);
            return null;
        }
    }

    public Trade executeMarketOrder(Order order) {
        if (order.getType() == com.sha.backend.domain.OrderSide.BUY) {
            return executeBuyOrder(order);
        } else {
            return executeSellOrder(order);
        }
    }

    private Trade executeBuyOrder(Order order) {
        com.sha.backend.domain.User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        double totalRequiredAmount = order.getPrice() * order.getQuantity();

        if (user.getKrwBalance() < totalRequiredAmount) {
            throw new IllegalArgumentException("잔고가 부족하여 주문을 체결할 수 없습니다.");
        }

        user.setKrwBalance(user.getKrwBalance() - totalRequiredAmount);
        userRepository.save(user);

        order.setStatus(OrderStatus.FILLED);
        order.setFilledAt(LocalDateTime.now());
        orderRepository.save(order);

        Holding holding = holdingRepository.findByUserIdAndSymbol(order.getUserId(), order.getSymbol())
                .orElseGet(() -> {
                    Holding newHolding = new Holding();
                    newHolding.setUserId(order.getUserId());
                    newHolding.setSymbol(order.getSymbol());
                    newHolding.setQuantity(0.0);
                    newHolding.setAvgBuyPrice(0.0);
                    return newHolding;
                });

        double currentTotalCost = holding.getQuantity() * holding.getAvgBuyPrice();
        double newTotalCost = currentTotalCost + totalRequiredAmount;
        double newQuantity = holding.getQuantity() + order.getQuantity();

        holding.setQuantity(newQuantity);
        holding.setAvgBuyPrice(newTotalCost / newQuantity);
        holdingRepository.save(holding);

        return createTradeRecord(order, totalRequiredAmount);
    }

    private Trade executeSellOrder(Order order) {
        Holding holding = holdingRepository.findByUserIdAndSymbol(order.getUserId(), order.getSymbol())
                .orElseThrow(() -> new IllegalArgumentException("보유하고 있지 않은 종목은 매도할 수 없습니다."));

        if (holding.getQuantity() < order.getQuantity()) {
            throw new IllegalArgumentException("보유 수량이 부족하여 주문을 체결할 수 없습니다.");
        }

        order.setStatus(OrderStatus.FILLED);
        order.setFilledAt(LocalDateTime.now());
        orderRepository.save(order);

        double newQuantity = holding.getQuantity() - order.getQuantity();
        holding.setQuantity(newQuantity);

        if (newQuantity == 0) {
            holdingRepository.delete(holding);
        } else {
            holdingRepository.save(holding);
        }

        double totalRevenueAmount = order.getPrice() * order.getQuantity();

        // 매도 성공 시 유저에게 원화 잔고 돌려주기 로직 추가
        com.sha.backend.domain.User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        user.setKrwBalance(user.getKrwBalance() + totalRevenueAmount);
        userRepository.save(user);

        return createTradeRecord(order, totalRevenueAmount);
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void matchLimitOrders() {
        double currentBtcPrice = 90000000.0;
        List<Order> allOrders = orderRepository.findAll();

        for (Order order : allOrders) {
            if (order.getStatus() == OrderStatus.PENDING && order.getOrderType() == com.sha.backend.domain.OrderType.LIMIT) {
                if (order.getType() == com.sha.backend.domain.OrderSide.BUY && order.getPrice() >= currentBtcPrice) {
                    executeBuyOrder(order);
                } else if (order.getType() == com.sha.backend.domain.OrderSide.SELL && order.getPrice() <= currentBtcPrice) {
                    executeSellOrder(order);
                }
            }
        }
    }

    private Trade createTradeRecord(Order order, double totalAmount) {
        Trade trade = new Trade();
        trade.setOrderId(order.getId());
        trade.setUserId(order.getUserId());
        trade.setSymbol(order.getSymbol());
        trade.setSide(order.getType().name());
        trade.setFilledPrice(order.getPrice());
        trade.setQuantity(order.getQuantity());
        trade.setTotalAmount(totalAmount);
        trade.setTradedAt(LocalDateTime.now());
        return tradeRepository.save(trade);
    }
}