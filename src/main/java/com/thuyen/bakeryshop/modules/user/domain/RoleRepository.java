package com.thuyen.bakeryshop.modules.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Optional<Role> findById(UUID id);
    Optional<Role> findByCode(String code);
}
