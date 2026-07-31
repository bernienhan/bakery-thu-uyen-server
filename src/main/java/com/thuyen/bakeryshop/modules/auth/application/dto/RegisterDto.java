package com.thuyen.bakeryshop.modules.auth.application.dto;

import java.util.Optional;

public record RegisterDto(
        String email,
        String phone,
        String fullName,
        String rawPassword
) {
}
