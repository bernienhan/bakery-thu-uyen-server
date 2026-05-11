package com.thuyen.bakeryshop.modules.user.api.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email
) {
}
