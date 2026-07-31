package com.thuyen.bakeryshop.modules.auth.infrastructure.redis;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.core.JacksonException;
import com.thuyen.bakeryshop.common.constant.RedisKeys;
import com.thuyen.bakeryshop.modules.auth.application.cache.AuthSessionCache;
import com.thuyen.bakeryshop.modules.auth.application.cache.CachedAuthSession;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RedisAuthSessionCache implements AuthSessionCache {

    private final StringRedisTemplate redisTemplate;
    private final JsonMapper jsonMapper;

    public RedisAuthSessionCache(
            StringRedisTemplate redisTemplate,
            JsonMapper jsonMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void save(CachedAuthSession session, Duration ttl) {
        if (!isPositive(ttl)) {
            deleteBySessionId(session.sessionId());
            return;
        }
        String key = RedisKeys.Auth.session(session.sessionId().toString());
        redisTemplate.opsForValue().set(key, writeJson(session), ttl);
    }

    @Override
    public Optional<CachedAuthSession> findBySessionId(UUID sessionId) {
        String key = RedisKeys.Auth.session(sessionId.toString());
        String value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value).map(this::readSession);
    }

    @Override
    public void deleteBySessionId(UUID sessionId) {
        redisTemplate.delete(RedisKeys.Auth.session(sessionId.toString()));
    }

    @Override
    public void addUserSession(UUID userId, UUID sessionId, Duration ttl) {
        if (!isPositive(ttl)) {
            return;
        }
        String key = RedisKeys.Auth.userSessions(userId.toString());
        redisTemplate.opsForSet().add(key, sessionId.toString());
        redisTemplate.expire(key, ttl);
    }

    @Override
    public Set<UUID> findSessionIdsByUserId(UUID userId) {
        String key = RedisKeys.Auth.userSessions(userId.toString());
        Set<String> sessionIds = redisTemplate.opsForSet().members(key);
        if (sessionIds == null) {
            return Set.of();
        }
        return sessionIds.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());
    }

    @Override
    public void removeUserSession(UUID userId, UUID sessionId) {
        String key = RedisKeys.Auth.userSessions(userId.toString());
        redisTemplate.opsForSet().remove(key, sessionId.toString());
    }

    @Override
    public void deleteUserSessions(UUID userId) {
        redisTemplate.delete(RedisKeys.Auth.userSessions(userId.toString()));
    }

    private String writeJson(CachedAuthSession session) {
        try {
            return jsonMapper.writeValueAsString(session);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize auth session cache", exception);
        }
    }

    private CachedAuthSession readSession(String value) {
        try {
            return jsonMapper.readValue(value, CachedAuthSession.class);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not deserialize auth session cache", exception);
        }
    }

    private boolean isPositive(Duration ttl) {
        return !ttl.isZero() && !ttl.isNegative();
    }
}
