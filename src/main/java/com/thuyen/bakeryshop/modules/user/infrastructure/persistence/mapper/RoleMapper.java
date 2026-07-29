package com.thuyen.bakeryshop.modules.user.infrastructure.persistence.mapper;

import com.thuyen.bakeryshop.modules.user.domain.model.Role;
import com.thuyen.bakeryshop.modules.user.infrastructure.persistence.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toDomain(RoleEntity entity) {
        return new Role(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
