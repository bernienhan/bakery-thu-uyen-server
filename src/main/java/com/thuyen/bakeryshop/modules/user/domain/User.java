package com.thuyen.bakeryshop.modules.user.domain;

import java.time.Instant;
import java.util.UUID;

public record User(
        UUID id,
        UUID roleId,
        String email,
        String phone,
        String fullName,
        String avatarUrl,
        UserStatus status,
        Instant emailVerifiedAt,
        Instant phoneVerifiedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
