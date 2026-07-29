package com.thuyen.bakeryshop.modules.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public record UserCredential(
        UUID userId,
        String passwordHash,
        Instant passwordChangedAt,
        int failedAttempts,
        Instant lockedUntil,
        Instant createdAt,
        Instant updatedAt
) {
}
