package com.thuyen.bakeryshop.modules.user.infrastructure.persistence.mapper;

import com.thuyen.bakeryshop.modules.user.domain.User;
import com.thuyen.bakeryshop.modules.user.infrastructure.persistence.entity.RoleEntity;
import com.thuyen.bakeryshop.modules.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getRole().getId(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getFullName(),
                entity.getAvatarUrl(),
                entity.getStatus(),
                entity.getEmailVerifiedAt(),
                entity.getPhoneVerifiedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public UserEntity toEntity(User user, RoleEntity role) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setRole(role);
        entity.setEmail(user.email());
        entity.setPhone(user.phone());
        entity.setFullName(user.fullName());
        entity.setAvatarUrl(user.avatarUrl());
        entity.setStatus(user.status());
        entity.setEmailVerifiedAt(user.emailVerifiedAt());
        entity.setPhoneVerifiedAt(user.phoneVerifiedAt());
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        return entity;
    }
}
