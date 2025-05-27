package com.example.persons.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Persons Service API")
                        .description("""
                                RESTful API for managing person records.
                                
                                ## Features
                                - CRUD operations for person records
                                - JWT-based authentication
                                - Rate limiting
                                - Redis caching
                                - Comprehensive monitoring
                                
                                ## Authentication
                                All endpoints require JWT authentication. Include the token in the Authorization header:
                                ```
                                Authorization: Bearer <your-token>
                                ```
                                
                                ## Rate Limiting
                                The API implements rate limiting to prevent abuse:
                                - 100 requests per minute per IP address
                                - Rate limit headers are included in responses
                                
                                ## Error Handling
                                The API uses standard HTTP status codes and returns detailed error messages:
                                - 400: Bad Request
                                - 401: Unauthorized
                                - 403: Forbidden
                                - 404: Not Found
                                - 429: Too Many Requests
                                - 500: Internal Server Error
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com")
                                .url("https://github.com/your-org/rest-app"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.example.com")
                                .description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token for authentication")));
    }
} 