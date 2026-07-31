package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.auth.domain.SecurityEventRepository;
import com.thuyen.bakeryshop.modules.auth.domain.model.SecurityEvent;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.SecurityEventEntity;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.mapper.SecurityEventMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityEventRepositoryImpl implements SecurityEventRepository {

    private final SecurityEventJpaRepository securityEventJpaRepository;
    private final SecurityEventMapper securityEventMapper;

    public SecurityEventRepositoryImpl(
            SecurityEventJpaRepository securityEventJpaRepository,
            SecurityEventMapper securityEventMapper
    ) {
        this.securityEventJpaRepository = securityEventJpaRepository;
        this.securityEventMapper = securityEventMapper;
    }

    @Override
    public SecurityEvent save(SecurityEvent securityEvent) {
        SecurityEventEntity entity = securityEventMapper.toEntity(securityEvent);
        SecurityEventEntity savedEntity = securityEventJpaRepository.save(entity);
        return securityEventMapper.toDomain(savedEntity);
    }
}
