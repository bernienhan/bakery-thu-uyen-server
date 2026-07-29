package com.thuyen.bakeryshop.modules.auth.domain.model;

import com.thuyen.bakeryshop.modules.auth.domain.enums.LoginCodeChannel;

import java.time.Instant;
import java.util.UUID;

public record LoginCode(
        UUID id,
        UUID userId,
        LoginCodeChannel channel,
        String target,
        String codeHash,
        int failedAttempts,
        Instant expiresAt,
        Instant usedAt,
        Instant createdAt
) {
}
