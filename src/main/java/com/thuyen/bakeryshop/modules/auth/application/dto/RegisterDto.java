package com.thuyen.bakeryshop.modules.auth.application.dto;

public record RegisterDto(
        String email,
        String phone,
        String fullName,
        String rawPassword
) {
}
