package com.thuyen.bakeryshop.modules.auth.application.dto;

public record LoginDto(
        String identifier,
        String rawPassword,
        String deviceId,
        String deviceName
) {
}
