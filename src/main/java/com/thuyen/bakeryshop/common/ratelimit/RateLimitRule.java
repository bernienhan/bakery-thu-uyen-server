package com.thuyen.bakeryshop.common.ratelimit;

import java.time.Duration;

public record RateLimitRule(
        int maxRequests,
        Duration window
) {
}
