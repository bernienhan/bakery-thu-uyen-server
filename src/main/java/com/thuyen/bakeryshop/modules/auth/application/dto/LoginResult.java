package com.thuyen.bakeryshop.modules.auth.application.dto;

import com.thuyen.bakeryshop.modules.user.application.dto.UserProfileDto;

import java.time.Instant;
import java.util.UUID;

public record LoginResult(
        UUID sessionId,
        Instant expiresAt,
        UserProfileDto user
) {
}
