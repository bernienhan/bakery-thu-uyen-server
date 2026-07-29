package com.thuyen.bakeryshop.modules.auth.domain.model;

import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionStatus;
import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionType;

import java.time.Instant;
import java.util.UUID;

public record AuthSession(
        UUID id,
        UUID userId,
        AuthSessionType sessionType,
        AuthSessionStatus status,
        String deviceId,
        String deviceName,
        String ipAddress,
        String userAgent,
        Instant lastActiveAt,
        Instant expiresAt,
        Instant revokedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
