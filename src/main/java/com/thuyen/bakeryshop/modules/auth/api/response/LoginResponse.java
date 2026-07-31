package com.thuyen.bakeryshop.modules.auth.api.response;

import com.thuyen.bakeryshop.modules.user.api.response.UserResponse;

public record LoginResponse(
        UserResponse user
) {
}
