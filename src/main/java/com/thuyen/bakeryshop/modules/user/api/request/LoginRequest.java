package com.thuyen.bakeryshop.modules.user.api.request;

public record LoginRequest(
        String email,
        String password
) {
}
