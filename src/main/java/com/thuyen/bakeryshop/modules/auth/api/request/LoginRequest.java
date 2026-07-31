package com.thuyen.bakeryshop.modules.auth.api.request;

public record LoginRequest(
        String phone,
        String email,
        String password
) {
}
