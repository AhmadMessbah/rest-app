package com.example.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenAccessingPublicEndpoint_thenSuccess() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void whenAccessingProtectedEndpointWithoutToken_thenUnauthorized() {
        webTestClient.get()
            .uri("/api/persons")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void whenAccessingSwaggerUi_thenSuccess() {
        webTestClient.get()
            .uri("/swagger-ui/index.html")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void whenAccessingApiDocs_thenSuccess() {
        webTestClient.get()
            .uri("/v3/api-docs")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void whenAccessingPublicApiEndpoint_thenSuccess() {
        webTestClient.get()
            .uri("/api/persons/public/health")
            .exchange()
            .expectStatus().isOk();
    }
} 