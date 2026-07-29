package com.thuyen.bakeryshop.modules.user.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Role(
        UUID id,
        String code,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
