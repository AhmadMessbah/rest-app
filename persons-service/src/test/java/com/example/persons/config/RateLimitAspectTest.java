package com.example.persons.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RateLimitAspectTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void whenRequestsWithinLimit_thenAllowRequests() throws Exception {
        // Clear any existing rate limit data
        redisTemplate.delete("rate_limit:127.0.0.1");

        // Make requests within the limit
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/api/persons"))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("X-Rate-Limit-Remaining"));
        }
    }

    @Test
    void whenRequestsExceedLimit_thenReturnTooManyRequests() throws Exception {
        // Clear any existing rate limit data
        redisTemplate.delete("rate_limit:127.0.0.1");

        // Set up a key with a high count to simulate exceeded limit
        String key = "rate_limit:127.0.0.1";
        redisTemplate.opsForValue().set(key, "101");
        redisTemplate.expire(key, 60, TimeUnit.SECONDS);

        // Make a request that should be rate limited
        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("X-Rate-Limit-Retry-After-Seconds"));
    }

    @Test
    void whenRateLimitExpires_thenAllowNewRequests() throws Exception {
        // Clear any existing rate limit data
        redisTemplate.delete("rate_limit:127.0.0.1");

        // Set up a key with a high count but short expiration
        String key = "rate_limit:127.0.0.1";
        redisTemplate.opsForValue().set(key, "101");
        redisTemplate.expire(key, 1, TimeUnit.SECONDS);

        // Wait for the key to expire
        Thread.sleep(1100);

        // Make a request that should now be allowed
        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Rate-Limit-Remaining"));
    }
} 