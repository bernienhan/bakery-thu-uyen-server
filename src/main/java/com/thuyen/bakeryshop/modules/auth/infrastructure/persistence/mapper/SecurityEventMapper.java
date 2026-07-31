package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.mapper;

import com.thuyen.bakeryshop.modules.auth.domain.model.SecurityEvent;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.SecurityEventEntity;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventMapper {

    public SecurityEvent toDomain(SecurityEventEntity entity) {
        return new SecurityEvent(
                entity.getId(),
                entity.getUserId(),
                entity.getEventCode(),
                entity.getTargetType(),
                entity.getTargetValue(),
                entity.getIpAddress(),
                entity.getUserAgent(),
                entity.getMetadata(),
                entity.getCreatedAt()
        );
    }

    public SecurityEventEntity toEntity(SecurityEvent securityEvent) {
        SecurityEventEntity entity = new SecurityEventEntity();
        entity.setId(securityEvent.id());
        entity.setUserId(securityEvent.userId());
        entity.setEventCode(securityEvent.eventCode());
        entity.setTargetType(securityEvent.targetType());
        entity.setTargetValue(securityEvent.targetValue());
        entity.setIpAddress(securityEvent.ipAddress());
        entity.setUserAgent(securityEvent.userAgent());
        entity.setMetadata(securityEvent.metadata());
        entity.setCreatedAt(securityEvent.createdAt());
        return entity;
    }
}
