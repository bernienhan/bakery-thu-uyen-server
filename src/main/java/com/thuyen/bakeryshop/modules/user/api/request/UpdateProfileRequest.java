package com.thuyen.bakeryshop.modules.user.api.request;

public record UpdateProfileRequest(
        String fullName,
        String phone
) {
}
