package com.sha.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String symbol; // 종목 코드 (예: KRW-BTC)

    @Enumerated(EnumType.STRING)
    private OrderSide type; // BUY(매수), SELL(매도)

    @Enumerated(EnumType.STRING)
    private OrderType orderType; // MARKET(시장가), LIMIT(지정가)

    private Double price; // 주문 가격
    private Double quantity; // 주문 수량

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING(대기), FILLED(체결), CANCELLED(취소)

    private LocalDateTime createdAt;
    private LocalDateTime filledAt; // 체결 시점

    @Version
    private Long version; // 🌟 기획서 4.3 반영: 동시 주문 방지용 낙관적 락 버전
}

