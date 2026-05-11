package com.thuyen.bakeryshop.modules.user.domain;

import java.util.Set;
import java.util.UUID;

public record User(
        UUID id,
        String fullName,
        String email,
        String phone,
        Set<Role> roles
) {
}
