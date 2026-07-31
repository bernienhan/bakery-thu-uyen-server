package com.thuyen.bakeryshop.modules.auth.application;

import com.thuyen.bakeryshop.common.exception.ApiException;
import com.thuyen.bakeryshop.common.exception.ErrorCode;
import com.thuyen.bakeryshop.modules.auth.application.dto.RegisterDto;
import com.thuyen.bakeryshop.modules.auth.domain.UserCredentialRepository;
import com.thuyen.bakeryshop.modules.auth.domain.model.UserCredential;
import com.thuyen.bakeryshop.modules.user.domain.RoleRepository;
import com.thuyen.bakeryshop.modules.user.domain.UserStatus;
import com.thuyen.bakeryshop.modules.user.domain.model.Role;
import com.thuyen.bakeryshop.modules.user.domain.model.User;
import com.thuyen.bakeryshop.modules.user.domain.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    private static final String DEFAULT_USER_ROLE_CODE = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserCredentialRepository userCredentialRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userCredentialRepository = userCredentialRepository;
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
}
