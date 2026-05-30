package com.sha.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Getter @Setter
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // 어떤 주문이 체결된 건지 기록
    private Long userId;
    private String symbol;

    private String side; // BUY, SELL
    private Double filledPrice; // 실제 체결된 가격
    private Double quantity; // 체결된 수량
    private Double totalAmount; // 총 체결 금액 (가격 * 수량)
    private LocalDateTime tradedAt;
}
