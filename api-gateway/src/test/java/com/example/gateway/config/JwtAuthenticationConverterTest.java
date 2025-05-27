package com.example.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthenticationConverterTest {

    private final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

    @Test
    void whenConvertingJwtWithRoles_thenAuthoritiesContainRoles() {
        // Create JWT claims with roles
        Map<String, Object> realmAccess = new HashMap<>();
        realmAccess.put("roles", List.of("user", "admin"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("realm_access", realmAccess);

        // Create JWT
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "RS256")
            .claim("sub", "user123")
            .claim("realm_access", realmAccess)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Convert JWT to authentication token
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) converter.convert(jwt);

        // Verify authorities
        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities())
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void whenConvertingJwtWithoutRoles_thenNoRoleAuthorities() {
        // Create JWT without roles
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "RS256")
            .claim("sub", "user123")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Convert JWT to authentication token
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) converter.convert(jwt);

        // Verify authorities
        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities()).isEmpty();
    }

    @Test
    void whenConvertingJwtWithEmptyRoles_thenNoRoleAuthorities() {
        // Create JWT with empty roles
        Map<String, Object> realmAccess = new HashMap<>();
        realmAccess.put("roles", List.of());

        Map<String, Object> claims = new HashMap<>();
        claims.put("realm_access", realmAccess);

        // Create JWT
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "RS256")
            .claim("sub", "user123")
            .claim("realm_access", realmAccess)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        // Convert JWT to authentication token
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) converter.convert(jwt);

        // Verify authorities
        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities()).isEmpty();
    }
} 