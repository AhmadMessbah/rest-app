package com.example.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("persons-service", r -> r.path("/api/persons/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("personsCircuitBreaker"))
                                .requestRateLimiter(rl -> rl.setRateLimiter(redisRateLimiter())))
                        .uri("lb://persons-service"))
                .route("image-requests-service", r -> r.path("/api/image-requests/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("imageRequestsCircuitBreaker"))
                                .requestRateLimiter(rl -> rl.setRateLimiter(redisRateLimiter())))
                        .uri("lb://image-requests-service"))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }
}