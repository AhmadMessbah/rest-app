package com.example.gateway.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestRedisConfig {

    private static final int REDIS_PORT = 6379;
    private GenericContainer<?> redisContainer;

    @PostConstruct
    public void startRedis() {
        redisContainer = new GenericContainer<>(DockerImageName.parse("redis:6.2-alpine"))
            .withExposedPorts(REDIS_PORT);
        redisContainer.start();
        System.setProperty("spring.redis.host", redisContainer.getHost());
        System.setProperty("spring.redis.port", String.valueOf(redisContainer.getMappedPort(REDIS_PORT)));
    }

    @PreDestroy
    public void stopRedis() {
        if (redisContainer != null) {
            redisContainer.stop();
        }
    }

    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
            .<String, String>newSerializationContext()
            .key(StringRedisSerializer.UTF_8)
            .value(StringRedisSerializer.UTF_8)
            .hashKey(StringRedisSerializer.UTF_8)
            .hashValue(StringRedisSerializer.UTF_8)
            .build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }
} 