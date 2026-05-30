package com.sha.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    // 외부 API를 호출할 때 쓸 도구를 스프링 상자에 넣어둡니다.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}