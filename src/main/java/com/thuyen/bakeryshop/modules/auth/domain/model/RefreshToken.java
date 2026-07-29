package com.thuyen.bakeryshop.modules.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public record RefreshToken(
        UUID id,
        UUID sessionId,
        UUID userId,
        String tokenHash,
        Instant expiresAt,
        Instant revokedAt,
        UUID replacedByTokenId,
        Instant createdAt
) {
}
