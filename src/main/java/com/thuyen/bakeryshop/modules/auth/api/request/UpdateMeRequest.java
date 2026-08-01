package com.thuyen.bakeryshop.modules.auth.api.request;

public record UpdateMeRequest(
        String fullName,
        String phone
) {
}
