package com.thuyen.bakeryshop.modules.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public record EmailVerificationToken(
        UUID id,
        UUID userId,
        String tokenHash,
        Instant expiresAt,
        Instant usedAt,
        Instant createdAt
) {
}
