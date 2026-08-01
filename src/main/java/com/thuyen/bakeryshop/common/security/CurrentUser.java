package com.thuyen.bakeryshop.common.security;

import java.util.UUID;

public record CurrentUser(
        UUID userId,
        UUID sessionId
) {
}
