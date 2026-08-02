package com.thuyen.bakeryshop.modules.auth.infrastructure.security;

import com.thuyen.bakeryshop.common.constant.ApiV1Paths;
import com.thuyen.bakeryshop.common.exception.ApiException;
import com.thuyen.bakeryshop.common.exception.ErrorCode;
import com.thuyen.bakeryshop.common.security.CurrentUser;
import com.thuyen.bakeryshop.common.security.CurrentUserContext;
import com.thuyen.bakeryshop.common.security.SecurityConstants;
import com.thuyen.bakeryshop.modules.auth.application.cache.AuthSessionCache;
import com.thuyen.bakeryshop.modules.auth.application.cache.CachedAuthSession;
import com.thuyen.bakeryshop.modules.auth.domain.AuthSessionRepository;
import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionStatus;
import com.thuyen.bakeryshop.modules.auth.domain.model.AuthSession;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuthSessionFilter extends OncePerRequestFilter {
    private static final String AUTH_ME_PATH = ApiV1Paths.Auth.BASE + ApiV1Paths.Auth.ME;

    private final AuthSessionCache authSessionCache;
    private final AuthSessionRepository authSessionRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public AuthSessionFilter(
            AuthSessionCache authSessionCache,
            AuthSessionRepository authSessionRepository,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.authSessionCache = authSessionCache;
        this.authSessionRepository = authSessionRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        CurrentUser currentUser;
        try {
            currentUser = resolveRequiredCurrentUser(request);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
            return;
        }

        try {
            CurrentUserContext.set(currentUser);
            filterChain.doFilter(request, response);
        } finally {
            CurrentUserContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !isProtectedPath(request);
    }

    private CurrentUser resolveRequiredCurrentUser(HttpServletRequest request) {
        UUID sessionId = readSessionId(request)
                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED));
        return resolveCurrentUser(sessionId);
    }

    private Optional<UUID> readSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> SecurityConstants.SESSION_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .filter(value -> !value.isBlank())
                .map(this::toUuid);
    }

    private UUID toUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }
    }

    private CurrentUser resolveCurrentUser(UUID sessionId) {
        Optional<CachedAuthSession> cachedSession = authSessionCache.findBySessionId(sessionId);
        if (cachedSession.isPresent()) {
            CachedAuthSession session = cachedSession.get();
            validateCachedSession(session);
            return new CurrentUser(session.userId(), session.sessionId());
        }

        AuthSession session = authSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ApiException(ErrorCode.SESSION_NOT_FOUND));
        validateSession(session);
        cacheSession(session);
        return new CurrentUser(session.userId(), session.id());
    }

    private void validateCachedSession(CachedAuthSession session) {
        if (session.status() == AuthSessionStatus.REVOKED) {
            throw new ApiException(ErrorCode.SESSION_REVOKED);
        }
        if (session.status() == AuthSessionStatus.EXPIRED || !session.expiresAt().isAfter(Instant.now())) {
            authSessionCache.deleteBySessionId(session.sessionId());
            authSessionCache.removeUserSession(session.userId(), session.sessionId());
            throw new ApiException(ErrorCode.SESSION_EXPIRED);
        }
    }

    private void validateSession(AuthSession session) {
        if (session.status() == AuthSessionStatus.REVOKED || session.revokedAt() != null) {
            throw new ApiException(ErrorCode.SESSION_REVOKED);
        }
        if (session.status() == AuthSessionStatus.EXPIRED || !session.expiresAt().isAfter(Instant.now())) {
            authSessionCache.deleteBySessionId(session.id());
            authSessionCache.removeUserSession(session.userId(), session.id());
            throw new ApiException(ErrorCode.SESSION_EXPIRED);
        }
    }

    private void cacheSession(AuthSession session) {
        Duration ttl = Duration.between(Instant.now(), session.expiresAt());
        CachedAuthSession cachedSession = new CachedAuthSession(
                session.id(),
                session.userId(),
                session.sessionType(),
                session.status(),
                session.deviceId(),
                session.deviceName(),
                session.expiresAt(),
                session.lastActiveAt()
        );
        authSessionCache.save(cachedSession, ttl);
        authSessionCache.addUserSession(session.userId(), session.id(), ttl);
    }

    private boolean isProtectedPath(HttpServletRequest request) {
        return AUTH_ME_PATH.equals(request.getRequestURI());
    }
}
