package com.thuyen.bakeryshop.modules.user.api.response;

import com.thuyen.bakeryshop.modules.user.domain.UserStatus;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String roleCode,
        String roleName,
        String fullName,
        String email,
        String phone,
        String avatarUrl,
        UserStatus status
) {
}
