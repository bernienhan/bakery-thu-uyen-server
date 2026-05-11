package com.thuyen.bakeryshop.modules.user.api.response;

public record LoginResponse(
        String accessToken,
        UserResponse user
) {
}
