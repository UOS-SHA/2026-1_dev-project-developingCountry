package com.sha.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HoldingResponseDto {
    private String symbol;        // 코인 심볼 (예: KRW-BTC)
    private Double quantity;      // 보유 수량
    private Double avgBuyPrice;   // 매수 평단가
}
