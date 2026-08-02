package com.thuyen.bakeryshop.common.ratelimit;

public interface RateLimitService {
    RateLimitResult consume(String key, RateLimitRule rule);

    default void check(String key, RateLimitRule rule) {
        RateLimitResult result = consume(key, rule);
        if (!result.allowed()) {
            throw new RateLimitException(result);
        }
    }
}
