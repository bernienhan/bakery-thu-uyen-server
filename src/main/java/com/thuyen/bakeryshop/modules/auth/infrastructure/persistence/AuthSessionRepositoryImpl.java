package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.auth.domain.AuthSessionRepository;
import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionStatus;
import com.thuyen.bakeryshop.modules.auth.domain.model.AuthSession;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.AuthSessionEntity;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.mapper.AuthSessionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthSessionRepositoryImpl implements AuthSessionRepository {

    private final AuthSessionJpaRepository authSessionJpaRepository;
    private final AuthSessionMapper authSessionMapper;

    public AuthSessionRepositoryImpl(
            AuthSessionJpaRepository authSessionJpaRepository,
            AuthSessionMapper authSessionMapper
    ) {
        this.authSessionJpaRepository = authSessionJpaRepository;
        this.authSessionMapper = authSessionMapper;
    }

    @Override
    public Optional<AuthSession> findById(UUID id) {
        return authSessionJpaRepository.findById(id).map(authSessionMapper::toDomain);
    }

    @Override
    public List<AuthSession> findActiveByUserId(UUID userId) {
        return authSessionJpaRepository.findByUserIdAndStatus(userId, AuthSessionStatus.ACTIVE)
                .stream()
                .map(authSessionMapper::toDomain)
                .toList();
    }

    @Override
    public AuthSession save(AuthSession session) {
        AuthSessionEntity entity = authSessionMapper.toEntity(session);
        AuthSessionEntity savedEntity = authSessionJpaRepository.save(entity);
        return authSessionMapper.toDomain(savedEntity);
    }
}
