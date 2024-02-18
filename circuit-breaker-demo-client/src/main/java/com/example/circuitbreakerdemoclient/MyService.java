package com.example.circuitbreakerdemoclient;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
@Slf4j
public class MyService {

    private final WebClient webClient;
    private final ReactiveCircuitBreaker readingListCircuitBreaker;

    public MyService(
            ReactiveCircuitBreakerFactory circuitBreakerFactory
    ) {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8090").build();
        this.readingListCircuitBreaker = circuitBreakerFactory.create("recommended");
    }

    public Mono<String> readingList() {
        return readingListCircuitBreaker.run(
                webClient
                        .get()
                        .uri("/recommended")
                        .retrieve()
                        .bodyToMono(String.class),
                throwable -> {
                    log.warn("Error making request to book service. ", throwable);
                    return Mono.just("Cloud Native Java (O'Reilly)");
                });
    }
}
