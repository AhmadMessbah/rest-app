package com.example.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class RouteConfigTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void shouldHaveAllRequiredRoutes() {
        List<Route> routes = routeLocator.getRoutes().collectList().block();
        assertThat(routes).isNotNull().hasSize(3);

        // Verify Persons Service Route
        Route personsRoute = routes.stream()
            .filter(route -> route.getId().equals("persons_service"))
            .findFirst()
            .orElseThrow();
        assertThat(personsRoute.getUri().toString()).isEqualTo("lb://persons-service");
        assertThat(personsRoute.getPredicate().toString()).contains("/api/persons/**");

        // Verify Image Requests Service Route
        Route imagesRoute = routes.stream()
            .filter(route -> route.getId().equals("image_requests_service"))
            .findFirst()
            .orElseThrow();
        assertThat(imagesRoute.getUri().toString()).isEqualTo("lb://image-requests-service");
        assertThat(imagesRoute.getPredicate().toString()).contains("/api/images/**");

        // Verify Swagger UI Route
        Route swaggerRoute = routes.stream()
            .filter(route -> route.getId().equals("swagger_ui"))
            .findFirst()
            .orElseThrow();
        assertThat(swaggerRoute.getUri().toString()).isEqualTo("lb://persons-service");
        assertThat(swaggerRoute.getPredicate().toString())
            .contains("/swagger-ui/**")
            .contains("/v3/api-docs/**");
    }
} 