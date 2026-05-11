package com.thuyen.bakeryshop.modules.user.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.user.domain.User;

import java.util.Set;

public class UserMapper {

    public User toDomain(UserJpaEntity entity) {
        return new User(entity.getId(), entity.getFullName(), entity.getEmail(), entity.getPhone(), Set.of());
    }
}
