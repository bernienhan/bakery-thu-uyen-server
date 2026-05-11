package com.thuyen.bakeryshop.modules.user.application.dto;

import java.util.UUID;

public record UserProfileDto(
        UUID id,
        String fullName,
        String email,
        String phone
) {
}
