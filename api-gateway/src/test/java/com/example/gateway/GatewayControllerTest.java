package com.example.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GatewayControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testPersonsServiceRoute() {
        webTestClient
            .get()
            .uri("/api/persons")
            .exchange()
            .expectStatus().isUnauthorized(); // انتظار می‌رود بدون JWT خطای 401 برگردد
    }

    @Test
    void testImageRequestsServiceRoute() {
        webTestClient
            .get()
            .uri("/api/image-requests")
            .exchange()
            .expectStatus().isUnauthorized(); // انتظار می‌رود بدون JWT خطای 401 برگردد
    }

    @Test
    void testSwaggerUIRoute() {
        webTestClient
            .get()
            .uri("/swagger-ui")
            .exchange()
            .expectStatus().isOk();
    }
}