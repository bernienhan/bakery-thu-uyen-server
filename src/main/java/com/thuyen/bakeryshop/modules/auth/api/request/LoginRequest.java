package com.thuyen.bakeryshop.modules.auth.api.request;

public record LoginRequest(
        String email,
        String password
) {
}
