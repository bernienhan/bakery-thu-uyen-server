package com.thuyen.bakeryshop.modules.user.domain;

import java.util.Set;

public record Role(
        String name,
        Set<Permission> permissions
) {
}
