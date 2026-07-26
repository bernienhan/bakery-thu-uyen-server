package com.thuyen.bakeryshop.modules.user.application.dto;

import com.thuyen.bakeryshop.modules.user.domain.UserStatus;

import java.util.UUID;

public record UserProfileDto(
        UUID id,
        UUID roleId,
        String fullName,
        String email,
        String phone,
        String avatarUrl,
        UserStatus status
) {
}
