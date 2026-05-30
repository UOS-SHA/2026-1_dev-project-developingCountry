package com.sha.backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Component
public class PriceFeederScheduler {

    private final RedisService redisService;
    private final RestTemplate restTemplate;

    // 업비트 실시간 시세 API 주소 (KRW-BTC = 원화 마켓의 비트코인)
    private final String UPBIT_API_URL = "https://api.upbit.com/v1/ticker?markets=KRW-BTC";

    public PriceFeederScheduler(RedisService redisService, RestTemplate restTemplate) {
        this.redisService = redisService;
        this.restTemplate = restTemplate;
    }

    // 업비트 초당 호출 제한을 고려하여 안전하게 1500ms(1.5초) 주기로 실행합니다.
    @Scheduled(fixedDelay = 1500)
    public void fetchRealUpbitPrice() {
        try {
            // 업비트 서버에 "지금 비트코인 얼마야?" 하고 전송 후 응답 받기
            List<Map<String, Object>> response = restTemplate.getForObject(UPBIT_API_URL, List.class);

            if (response != null && !response.isEmpty()) {
                // 업비트가 주는 복잡한 데이터 주머니에서 'trade_price'(현재가)만 쏙 추출
                Map<String, Object> tickerData = response.get(0);
                double realPrice = Double.parseDouble(tickerData.get("trade_price").toString());

                // 우리가 만든 고속 메모리 시세판에 진짜 가격 저장!
                redisService.setPrice("KRW-BTC", realPrice);
                System.out.println("현재 업비트 BTC 진짜 가격: " + realPrice + "원");
            }
        } catch (Exception e) {
            // 인터넷 연결 지연이나 업비트 서버 과부하 시 에러로 뻗지 않고 로그만 찍고 넘어가게 처리
            System.err.println("업비트 시세 가져오기 실패: " + e.getMessage());
        }
    }
}