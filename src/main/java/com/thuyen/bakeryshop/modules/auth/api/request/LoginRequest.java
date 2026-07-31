package com.thuyen.bakeryshop.modules.auth.api.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String identifier,
        @NotBlank
        String password,
        String deviceId,
        String deviceName
) {
}
