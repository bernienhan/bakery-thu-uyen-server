package com.thuyen.bakeryshop.modules.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public record AuthProvider(
        UUID id,
        UUID userId,
        String providerCode,
        String providerUserId,
        String providerEmail,
        Instant createdAt
) {
}
