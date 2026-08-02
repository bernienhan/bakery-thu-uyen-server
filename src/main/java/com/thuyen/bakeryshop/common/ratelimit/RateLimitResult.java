package com.thuyen.bakeryshop.common.ratelimit;

import java.time.Duration;

public record RateLimitResult(
        String key,
        boolean allowed,
        long currentRequests,
        int maxRequests,
        Duration retryAfter
) {
}
