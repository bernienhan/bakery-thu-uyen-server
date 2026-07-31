package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.mapper;

import com.thuyen.bakeryshop.modules.auth.domain.model.AuthSession;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.AuthSessionEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthSessionMapper {

    public AuthSession toDomain(AuthSessionEntity entity) {
        return new AuthSession(
                entity.getId(),
                entity.getUserId(),
                entity.getSessionType(),
                entity.getStatus(),
                entity.getDeviceId(),
                entity.getDeviceName(),
                entity.getIpAddress(),
                entity.getUserAgent(),
                entity.getLastActiveAt(),
                entity.getExpiresAt(),
                entity.getRevokedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public AuthSessionEntity toEntity(AuthSession session) {
        AuthSessionEntity entity = new AuthSessionEntity();
        entity.setId(session.id());
        entity.setUserId(session.userId());
        entity.setSessionType(session.sessionType());
        entity.setStatus(session.status());
        entity.setDeviceId(session.deviceId());
        entity.setDeviceName(session.deviceName());
        entity.setIpAddress(session.ipAddress());
        entity.setUserAgent(session.userAgent());
        entity.setLastActiveAt(session.lastActiveAt());
        entity.setExpiresAt(session.expiresAt());
        entity.setRevokedAt(session.revokedAt());
        entity.setCreatedAt(session.createdAt());
        entity.setUpdatedAt(session.updatedAt());
        return entity;
    }
}
