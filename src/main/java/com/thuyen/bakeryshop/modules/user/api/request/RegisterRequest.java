package com.thuyen.bakeryshop.modules.user.api.request;

public record RegisterRequest(
        String fullName,
        String email,
        String password
) {
}
