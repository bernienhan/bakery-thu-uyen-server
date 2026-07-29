package com.thuyen.bakeryshop.modules.auth.domain;

import com.thuyen.bakeryshop.modules.auth.domain.model.AuthSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthSessionRepository {
    Optional<AuthSession> findById(UUID id);

    List<AuthSession> findActiveByUserId(UUID userId);

    AuthSession save(AuthSession session);
}
