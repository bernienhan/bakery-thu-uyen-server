package com.thuyen.bakeryshop.modules.auth.application;

import com.thuyen.bakeryshop.common.exception.ApiException;
import com.thuyen.bakeryshop.common.exception.ErrorCode;
import com.thuyen.bakeryshop.modules.auth.application.cache.AuthSessionCache;
import com.thuyen.bakeryshop.modules.auth.application.cache.CachedAuthSession;
import com.thuyen.bakeryshop.modules.auth.application.dto.LoginDto;
import com.thuyen.bakeryshop.modules.auth.application.dto.LoginResult;
import com.thuyen.bakeryshop.modules.auth.application.dto.RegisterDto;
import com.thuyen.bakeryshop.modules.auth.application.dto.UpdateMeDto;
import com.thuyen.bakeryshop.modules.auth.domain.AuthSessionRepository;
import com.thuyen.bakeryshop.modules.auth.domain.SecurityEventRepository;
import com.thuyen.bakeryshop.modules.auth.domain.UserCredentialRepository;
import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionStatus;
import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionType;
import com.thuyen.bakeryshop.modules.auth.domain.model.AuthSession;
import com.thuyen.bakeryshop.modules.auth.domain.model.SecurityEvent;
import com.thuyen.bakeryshop.modules.auth.domain.model.UserCredential;
import com.thuyen.bakeryshop.modules.user.domain.RoleRepository;
import com.thuyen.bakeryshop.modules.user.domain.UserPolicy;
import com.thuyen.bakeryshop.modules.user.domain.UserStatus;
import com.thuyen.bakeryshop.modules.user.application.dto.UserProfileDto;
import com.thuyen.bakeryshop.modules.user.application.mapper.UserProfileMapper;
import com.thuyen.bakeryshop.modules.user.domain.model.Role;
import com.thuyen.bakeryshop.modules.user.domain.model.User;
import com.thuyen.bakeryshop.modules.user.domain.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private static final String DEFAULT_USER_ROLE_CODE = "USER";
    private static final Duration SESSION_TTL = Duration.ofDays(7);
    private static final String LOGIN_SUCCESS_EVENT = "LOGIN_SUCCESS";
    private static final String LOGIN_FAILED_EVENT = "LOGIN_FAILED";
    private static final String TARGET_TYPE_EMAIL = "EMAIL";
    private static final String TARGET_TYPE_PHONE = "PHONE";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserPolicy userPolicy;
    private final UserProfileMapper userProfileMapper;
    private final UserCredentialRepository userCredentialRepository;
    private final AuthSessionRepository authSessionRepository;
    private final SecurityEventRepository securityEventRepository;
    private final AuthSessionCache authSessionCache;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserPolicy userPolicy,
            UserProfileMapper userProfileMapper,
            UserCredentialRepository userCredentialRepository,
            AuthSessionRepository authSessionRepository,
            SecurityEventRepository securityEventRepository,
            AuthSessionCache authSessionCache,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userPolicy = userPolicy;
        this.userProfileMapper = userProfileMapper;
        this.userCredentialRepository = userCredentialRepository;
        this.authSessionRepository = authSessionRepository;
        this.securityEventRepository = securityEventRepository;
        this.authSessionCache = authSessionCache;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterDto command) {
        userRepository.findByEmail(command.email())
                .ifPresent(user -> {
                    throw new ApiException(ErrorCode.EMAIL_ALREADY_EXISTS);
                });

        userRepository.findByPhone(command.phone()).ifPresent(user -> {
            throw new ApiException(ErrorCode.PHONE_ALREADY_EXISTS);
        });


        Role userRole = roleRepository.findByCode(DEFAULT_USER_ROLE_CODE)
                .orElseThrow(() -> new ApiException(ErrorCode.DEFAULT_ROLE_NOT_FOUND));

        Instant now = Instant.now();
        User user = new User(
                UUID.randomUUID(),
                userRole.id(),
                command.email(),
                command.phone(),
                command.fullName(),
                null,
                UserStatus.ACTIVE,
                null,
                null,
                now,
                now
        );

        User savedUser = userRepository.save(user);

        UserCredential credential = new UserCredential(
                savedUser.id(),
                passwordEncoder.encode(command.rawPassword()),
                now,
                0,
                null,
                now,
                now
        );
        userCredentialRepository.save(credential);
    }

    public LoginResult login(LoginDto dto, String ipAddress, String userAgent) {
        Optional<User> optionalUser = findUserByIdentifier(dto.identifier());
        if (optionalUser.isEmpty()) {
            saveLoginEvent(null, dto.identifier(), ipAddress, userAgent, false);
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }

        User user = optionalUser.get();
        if (!userPolicy.canLogin(user)) {
            saveLoginEvent(user.id(), dto.identifier(), ipAddress, userAgent, false);
            throw loginStatusError(user);
        }

        UserCredential credential = userCredentialRepository.findByUserId(user.id())
                .orElseThrow(() -> {
                    saveLoginEvent(user.id(), dto.identifier(), ipAddress, userAgent, false);
                    return new ApiException(ErrorCode.INVALID_CREDENTIALS);
                });

        if (!passwordEncoder.matches(dto.rawPassword(), credential.passwordHash())) {
            saveLoginEvent(user.id(), dto.identifier(), ipAddress, userAgent, false);
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }

        Instant now = Instant.now();
        Instant expiresAt = now.plus(SESSION_TTL);
        AuthSession session = new AuthSession(
                UUID.randomUUID(),
                user.id(),
                AuthSessionType.WEB,
                AuthSessionStatus.ACTIVE,
                dto.deviceId(),
                dto.deviceName(),
                ipAddress,
                userAgent,
                now,
                expiresAt,
                null,
                now,
                now
        );

        AuthSession savedSession = authSessionRepository.save(session);
        cacheSession(savedSession);
        saveLoginEvent(user.id(), dto.identifier(), ipAddress, userAgent, true);
        Role userRole = roleRepository.findById(user.roleId())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));

        return new LoginResult(
                savedSession.id(),
                savedSession.expiresAt(),
                userProfileMapper.toDto(user, userRole)
        );
    }

    public UserProfileDto me(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return userProfileMapper.toDto(user, findUserRole(user));
    }

    @Transactional
    public UserProfileDto updateMe(UUID userId, UpdateMeDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (!userPolicy.canUpdateProfile(user)) {
            throw loginStatusError(user);
        }

        String newPhone = dto.phone() != null ? dto.phone() : user.phone();
        String newFullName = dto.fullName() != null ? dto.fullName() : user.fullName();

        if (newPhone != null && !newPhone.equals(user.phone())) {
            userRepository.findByPhone(newPhone).ifPresent(existingUser -> {
                if (!existingUser.id().equals(user.id())) {
                    throw new ApiException(ErrorCode.PHONE_ALREADY_EXISTS);
                }
            });
        }

        Instant now = Instant.now();
        User updatedUser = new User(
                user.id(),
                user.roleId(),
                user.email(),
                newPhone,
                newFullName,
                user.avatarUrl(),
                user.status(),
                user.emailVerifiedAt(),
                user.phoneVerifiedAt(),
                user.createdAt(),
                now
        );

        User savedUser = userRepository.save(updatedUser);
        return userProfileMapper.toDto(savedUser, findUserRole(savedUser));
    }

    private Optional<User> findUserByIdentifier(String identifier) {
        if (isEmail(identifier)) {
            return userRepository.findByEmail(identifier);
        }
        return userRepository.findByPhone(identifier);
    }

    private boolean isEmail(String identifier) {
        return identifier != null && identifier.contains("@");
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

    private void saveLoginEvent(
            UUID userId,
            String identifier,
            String ipAddress,
            String userAgent,
            boolean success
    ) {
        SecurityEvent event = new SecurityEvent(
                UUID.randomUUID(),
                userId,
                success ? LOGIN_SUCCESS_EVENT : LOGIN_FAILED_EVENT,
                isEmail(identifier) ? TARGET_TYPE_EMAIL : TARGET_TYPE_PHONE,
                identifier,
                ipAddress,
                userAgent,
                Map.of("success", success),
                Instant.now()
        );
        securityEventRepository.save(event);
    }

    private ApiException loginStatusError(User user) {
        return switch (user.status()) {
            case INACTIVE -> new ApiException(ErrorCode.USER_INACTIVE);
            case BANNED -> new ApiException(ErrorCode.USER_BANNED);
            case DELETED -> new ApiException(ErrorCode.USER_DELETED);
            case ACTIVE -> new ApiException(ErrorCode.INVALID_CREDENTIALS);
        };
    }

    private Role findUserRole(User user) {
        return roleRepository.findById(user.roleId())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
