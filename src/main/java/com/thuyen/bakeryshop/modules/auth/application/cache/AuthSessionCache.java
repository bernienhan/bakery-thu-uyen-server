package com.thuyen.bakeryshop.modules.auth.application.cache;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AuthSessionCache {
    void save(CachedAuthSession session, Duration ttl);

    Optional<CachedAuthSession> findBySessionId(UUID sessionId);

    void deleteBySessionId(UUID sessionId);

    void addUserSession(UUID userId, UUID sessionId, Duration ttl);

    Set<UUID> findSessionIdsByUserId(UUID userId);

    void removeUserSession(UUID userId, UUID sessionId);

    void deleteUserSessions(UUID userId);
}
