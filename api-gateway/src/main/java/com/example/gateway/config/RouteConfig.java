package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Persons Service Routes
            .route("persons_service", r -> r
                .path("/api/persons/**")
                .filters(f -> f
                    .stripPrefix(1)
                    .addRequestHeader("X-Service-Name", "persons-service")
                )
                .uri("lb://persons-service")
            )
            // Image Requests Service Routes
            .route("image_requests_service", r -> r
                .path("/api/images/**")
                .filters(f -> f
                    .stripPrefix(1)
                    .addRequestHeader("X-Service-Name", "image-requests-service")
                )
                .uri("lb://image-requests-service")
            )
            // Swagger UI Routes
            .route("swagger_ui", r -> r
                .path("/swagger-ui/**", "/v3/api-docs/**")
                .filters(f -> f
                    .rewritePath("/swagger-ui/(?<path>.*)", "/persons-service/swagger-ui/${path}")
                    .rewritePath("/v3/api-docs/(?<path>.*)", "/persons-service/v3/api-docs/${path}")
                )
                .uri("lb://persons-service")
            )
            .build();
    }
} 