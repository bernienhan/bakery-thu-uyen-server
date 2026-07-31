package com.thuyen.bakeryshop.modules.auth.application.cache;

import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionStatus;
import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionType;

import java.time.Instant;
import java.util.UUID;

public record CachedAuthSession(
        UUID sessionId,
        UUID userId,
        AuthSessionType sessionType,
        AuthSessionStatus status,
        String deviceId,
        String deviceName,
        Instant expiresAt,
        Instant lastActiveAt
) {
}
