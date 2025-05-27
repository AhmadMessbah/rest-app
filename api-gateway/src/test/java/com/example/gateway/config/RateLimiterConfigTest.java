package com.example.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class RateLimiterConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenExceedingRateLimit_thenTooManyRequests() {
        // Make requests up to the rate limit
        IntStream.range(0, 10).forEach(i -> {
            webTestClient.get()
                .uri("/api/persons/public/health")
                .exchange()
                .expectStatus().isOk();
        });

        // Next request should be rate limited
        webTestClient.get()
            .uri("/api/persons/public/health")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void whenUsingDifferentIpAddresses_thenSeparateRateLimits() {
        // Make requests from different IP addresses
        String[] ips = {"192.168.1.1", "192.168.1.2", "192.168.1.3"};

        for (String ip : ips) {
            // Each IP should be able to make requests up to the rate limit
            IntStream.range(0, 10).forEach(i -> {
                webTestClient.get()
                    .uri("/api/persons/public/health")
                    .header("X-Forwarded-For", ip)
                    .exchange()
                    .expectStatus().isOk();
            });
        }
    }

    @Test
    void whenUsingDifferentUserIds_thenSeparateRateLimits() {
        // Make requests from different user IDs
        String[] userIds = {"user1", "user2", "user3"};

        for (String userId : userIds) {
            // Each user should be able to make requests up to the rate limit
            IntStream.range(0, 10).forEach(i -> {
                webTestClient.get()
                    .uri("/api/persons/public/health")
                    .header("X-User-ID", userId)
                    .exchange()
                    .expectStatus().isOk();
            });
        }
    }

    @Test
    void whenRateLimitExpires_thenAllowNewRequests() throws InterruptedException {
        // Make requests up to the rate limit
        IntStream.range(0, 10).forEach(i -> {
            webTestClient.get()
                .uri("/api/persons/public/health")
                .exchange()
                .expectStatus().isOk();
        });

        // Next request should be rate limited
        webTestClient.get()
            .uri("/api/persons/public/health")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS);

        // Wait for rate limit to reset (1 second in test configuration)
        Thread.sleep(1100);

        // Should be able to make new requests
        webTestClient.get()
            .uri("/api/persons/public/health")
            .exchange()
            .expectStatus().isOk();
    }
} 