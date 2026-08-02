package com.thuyen.bakeryshop.modules.auth.application;

import com.thuyen.bakeryshop.common.exception.ErrorCode;
import com.thuyen.bakeryshop.modules.auth.application.cache.AuthSessionCache;
import com.thuyen.bakeryshop.modules.auth.application.dto.LoginDto;
import com.thuyen.bakeryshop.modules.auth.application.dto.LoginResult;
import com.thuyen.bakeryshop.modules.auth.application.dto.RegisterDto;
import com.thuyen.bakeryshop.modules.auth.domain.AuthSessionRepository;
import com.thuyen.bakeryshop.modules.auth.domain.SecurityEventRepository;
import com.thuyen.bakeryshop.modules.auth.domain.UserCredentialRepository;
import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionStatus;
import com.thuyen.bakeryshop.modules.auth.domain.model.AuthSession;
import com.thuyen.bakeryshop.modules.auth.domain.model.SecurityEvent;
import com.thuyen.bakeryshop.modules.auth.domain.model.UserCredential;
import com.thuyen.bakeryshop.modules.user.application.mapper.UserProfileMapper;
import com.thuyen.bakeryshop.modules.user.domain.RoleRepository;
import com.thuyen.bakeryshop.modules.user.domain.UserPolicy;
import com.thuyen.bakeryshop.modules.user.domain.UserRepository;
import com.thuyen.bakeryshop.modules.user.domain.UserStatus;
import com.thuyen.bakeryshop.modules.user.domain.model.Role;
import com.thuyen.bakeryshop.modules.user.domain.model.User;
import com.thuyen.bakeryshop.support.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest extends BaseUnitTest {
    private static final UUID ROLE_ID = UUID.fromString("8db2a093-1b8f-48f3-9f32-7c5c67f71e11");
    private static final UUID USER_ID = UUID.fromString("d58f2ce7-8452-4c2b-9774-d413be7d2d2f");

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserCredentialRepository userCredentialRepository;
    @Mock
    private AuthSessionRepository authSessionRepository;
    @Mock
    private SecurityEventRepository securityEventRepository;
    @Mock
    private AuthSessionCache authSessionCache;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                roleRepository,
                new UserPolicy(),
                new UserProfileMapper(),
                userCredentialRepository,
                authSessionRepository,
                securityEventRepository,
                authSessionCache,
                passwordEncoder
        );
    }

    @Test
    void register_shouldCreateUserAndCredential_whenEmailAndPhoneAreAvailable() {
        RegisterDto dto = new RegisterDto(
                "new-user@gmail.com",
                "0901234567",
                "New User",
                "password123"
        );
        Role userRole = userRole();
        User savedUser = user(
                USER_ID,
                userRole.id(),
                dto.email(),
                dto.phone(),
                dto.fullName(),
                UserStatus.ACTIVE
        );

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userRepository.findByPhone(dto.phone())).thenReturn(Optional.empty());
        when(roleRepository.findByCode("USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(dto.rawPassword())).thenReturn("encoded-password");

        authService.register(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UserCredential> credentialCaptor = ArgumentCaptor.forClass(UserCredential.class);
        verify(userRepository).save(userCaptor.capture());
        verify(userCredentialRepository).save(credentialCaptor.capture());

        assertThat(userCaptor.getValue().email()).isEqualTo(dto.email());
        assertThat(userCaptor.getValue().roleId()).isEqualTo(userRole.id());
        assertThat(userCaptor.getValue().status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(credentialCaptor.getValue().userId()).isEqualTo(USER_ID);
        assertThat(credentialCaptor.getValue().passwordHash()).isEqualTo("encoded-password");
    }

    @Test
    void register_shouldThrowEmailAlreadyExists_whenEmailExists() {
        RegisterDto dto = new RegisterDto(
                "old-user@gmail.com",
                "0901234567",
                "Old User",
                "password123"
        );
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(activeUser()));

        assertApiException(ErrorCode.EMAIL_ALREADY_EXISTS, () -> authService.register(dto));

        verify(userRepository, never()).save(any(User.class));
        verify(userCredentialRepository, never()).save(any(UserCredential.class));
    }

    @Test
    void login_shouldCreateSessionAndReturnUserProfile_whenCredentialsAreValid() {
        LoginDto dto = new LoginDto("user@gmail.com", "password123", null, null);
        User user = activeUser();
        Role role = userRole();
        AuthSession savedSession = activeSession(user.id());

        when(userRepository.findByEmail(dto.identifier())).thenReturn(Optional.of(user));
        when(userCredentialRepository.findByUserId(user.id())).thenReturn(Optional.of(credential(user.id())));
        when(passwordEncoder.matches(dto.rawPassword(), "encoded-password")).thenReturn(true);
        when(authSessionRepository.save(any(AuthSession.class))).thenReturn(savedSession);
        when(roleRepository.findById(user.roleId())).thenReturn(Optional.of(role));

        LoginResult result = authService.login(dto, "127.0.0.1", "JUnit");

        assertThat(result.sessionId()).isEqualTo(savedSession.id());
        assertThat(result.user().email()).isEqualTo(user.email());
        assertThat(result.user().roleCode()).isEqualTo(role.code());

        verify(authSessionRepository).save(any(AuthSession.class));
        verify(authSessionCache).save(any(), any(Duration.class));
        verify(authSessionCache).addUserSession(eq(user.id()), eq(savedSession.id()), any(Duration.class));
        verify(securityEventRepository).save(any(SecurityEvent.class));
    }

    @Test
    void login_shouldThrowInvalidCredentialsAndSaveFailedEvent_whenPasswordIsWrong() {
        LoginDto dto = new LoginDto("user@gmail.com", "wrong-password", null, null);
        User user = activeUser();

        when(userRepository.findByEmail(dto.identifier())).thenReturn(Optional.of(user));
        when(userCredentialRepository.findByUserId(user.id())).thenReturn(Optional.of(credential(user.id())));
        when(passwordEncoder.matches(dto.rawPassword(), "encoded-password")).thenReturn(false);

        assertApiException(ErrorCode.INVALID_CREDENTIALS, () -> authService.login(dto, "127.0.0.1", "JUnit"));

        verify(securityEventRepository).save(any(SecurityEvent.class));
        verify(authSessionRepository, never()).save(any(AuthSession.class));
        verify(authSessionCache, never()).save(any(), any(Duration.class));
    }

    private User activeUser() {
        return user(
                USER_ID,
                ROLE_ID,
                "user@gmail.com",
                "0901234567",
                "Test User",
                UserStatus.ACTIVE
        );
    }

    private User user(UUID id, UUID roleId, String email, String phone, String fullName, UserStatus status) {
        Instant now = Instant.now();
        return new User(id, roleId, email, phone, fullName, null, status, null, null, now, now);
    }

    private Role userRole() {
        Instant now = Instant.now();
        return new Role(ROLE_ID, "USER", "User", "Default user role", now, now);
    }

    private UserCredential credential(UUID userId) {
        Instant now = Instant.now();
        return new UserCredential(userId, "encoded-password", now, 0, null, now, now);
    }

    private AuthSession activeSession(UUID userId) {
        Instant now = Instant.now();
        return new AuthSession(
                UUID.fromString("331a01aa-b832-433f-9288-5f9bc0897f8d"),
                userId,
                null,
                AuthSessionStatus.ACTIVE,
                null,
                null,
                "127.0.0.1",
                "JUnit",
                now,
                now.plus(Duration.ofDays(7)),
                null,
                now,
                now
        );
    }
}
