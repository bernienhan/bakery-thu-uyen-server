package com.thuyen.bakeryshop.common.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RedisRateLimitService implements RateLimitService {
    private final StringRedisTemplate redisTemplate;

    public RedisRateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RateLimitResult consume(String key, RateLimitRule rule) {
        Long currentRequests = redisTemplate.opsForValue().increment(key);
        long current = currentRequests == null ? 1 : currentRequests;

        if (current == 1) {
            redisTemplate.expire(key, rule.window());
        }

        boolean allowed = current <= rule.maxRequests();
        return new RateLimitResult(
                key,
                allowed,
                current,
                rule.maxRequests(),
                allowed ? Duration.ZERO : retryAfter(key, rule.window())
        );
    }

    private Duration retryAfter(String key, Duration fallback) {
        Long seconds = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (seconds == null || seconds <= 0) {
            return fallback;
        }
        return Duration.ofSeconds(seconds);
    }
}
