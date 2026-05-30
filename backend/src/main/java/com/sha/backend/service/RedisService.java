package com.sha.backend.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis에 시세 저장하기 (Key: 종목코드, Value: 현재가)
    public void setPrice(String symbol, double price) {
        redisTemplate.opsForValue().set("PRICE:" + symbol, String.valueOf(price));
    }

    // Redis에서 시세 꺼내오기
    public double getPrice(String symbol) {
        String priceStr = redisTemplate.opsForValue().get("PRICE:" + symbol);
        if (priceStr == null) {
            return 90000000.0; // Redis에 값이 없을 때 내보낼 기본 기준가
        }
        return Double.parseDouble(priceStr);
    }
}