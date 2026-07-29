package com.thuyen.bakeryshop.modules.auth.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record SecurityEvent(
        UUID id,
        UUID userId,
        String eventCode,
        String targetType,
        String targetValue,
        String ipAddress,
        String userAgent,
        Map<String, Object> metadata,
        Instant createdAt
) {
}
