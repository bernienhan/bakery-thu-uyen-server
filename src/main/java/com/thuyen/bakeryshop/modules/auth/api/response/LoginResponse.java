package com.thuyen.bakeryshop.modules.auth.api.response;

import com.thuyen.bakeryshop.modules.user.api.response.UserResponse;

import java.time.Instant;
import java.util.UUID;

public record LoginResponse(
        UUID sessionId,
        Instant expiresAt,
        UserResponse user
) {
}
