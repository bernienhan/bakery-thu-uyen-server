package com.thuyen.bakeryshop.modules.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public record SecurityBlock(
        UUID id,
        UUID userId,
        String targetType,
        String targetValue,
        String reasonCode,
        Instant blockedUntil,
        Instant revokedAt,
        UUID createdBy,
        Instant createdAt
) {
}
