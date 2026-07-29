package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.mapper;

import com.thuyen.bakeryshop.modules.auth.domain.model.UserCredential;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.UserCredentialEntity;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialMapper {

    public UserCredential toDomain(UserCredentialEntity entity) {
        return new UserCredential(
                entity.getUserId(),
                entity.getPasswordHash(),
                entity.getPasswordChangedAt(),
                entity.getFailedAttempts(),
                entity.getLockedUntil(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public UserCredentialEntity toEntity(UserCredential credential) {
        UserCredentialEntity entity = new UserCredentialEntity();
        entity.setUserId(credential.userId());
        entity.setPasswordHash(credential.passwordHash());
        entity.setPasswordChangedAt(credential.passwordChangedAt());
        entity.setFailedAttempts(credential.failedAttempts());
        entity.setLockedUntil(credential.lockedUntil());
        entity.setCreatedAt(credential.createdAt());
        entity.setUpdatedAt(credential.updatedAt());
        return entity;
    }
}
