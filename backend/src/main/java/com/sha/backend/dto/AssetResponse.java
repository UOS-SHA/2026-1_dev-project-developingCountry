package com.sha.backend.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AssetResponse {
    private Double krwBalance;                 // 남은 원화 현금
    private List<HoldingResponseDto> holdings; // 정제된 보유 코인 목록
}
