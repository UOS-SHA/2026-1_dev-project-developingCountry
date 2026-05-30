package com.sha.backend.controller;

import com.sha.backend.domain.Holding;
import com.sha.backend.domain.User;
import com.sha.backend.dto.AssetResponse;
import com.sha.backend.dto.HoldingResponseDto;
import com.sha.backend.repository.HoldingRepository;
import com.sha.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/holdings")
public class HoldingController {

    private final HoldingRepository holdingRepository;
    private final UserRepository userRepository;

    public HoldingController(HoldingRepository holdingRepository, UserRepository userRepository) {
        this.holdingRepository = holdingRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public AssetResponse getUserAssets(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<Holding> holdings = holdingRepository.findByUserId(userId);

        // 엔티티 목록을 안전한 DTO 목록으로 변환하여 매핑
        List<HoldingResponseDto> holdingDtos = new ArrayList<>();
        for (Holding h : holdings) {
            HoldingResponseDto hDto = new HoldingResponseDto();
            hDto.setSymbol(h.getSymbol());
            hDto.setQuantity(h.getQuantity());
            hDto.setAvgBuyPrice(h.getAvgBuyPrice());
            holdingDtos.add(hDto);
        }

        AssetResponse response = new AssetResponse();
        response.setKrwBalance(user.getKrwBalance());
        response.setHoldings(holdingDtos);

        return response;
    }
}