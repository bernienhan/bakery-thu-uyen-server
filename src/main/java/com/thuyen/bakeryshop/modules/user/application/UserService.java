package com.thuyen.bakeryshop.modules.user.application;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.thuyen.bakeryshop.common.exception.ApiException;
import com.thuyen.bakeryshop.common.exception.ErrorCode;
import com.thuyen.bakeryshop.modules.user.application.dto.UserProfileDto;
import com.thuyen.bakeryshop.modules.user.application.mapper.UserProfileMapper;
import com.thuyen.bakeryshop.modules.user.domain.RoleRepository;
import com.thuyen.bakeryshop.modules.user.domain.model.User;
import com.thuyen.bakeryshop.modules.user.domain.model.Role;
import com.thuyen.bakeryshop.modules.user.domain.UserPolicy;
import com.thuyen.bakeryshop.modules.user.domain.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserPolicy userPolicy;
    private final UserProfileMapper userProfileMapper;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserPolicy userPolicy,
            UserProfileMapper userProfileMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userPolicy = userPolicy;
        this.userProfileMapper = userProfileMapper;
    }

    public UserProfileDto getProfile(UUID userId) {
        User user = findExistingUser(userId);
        return userProfileMapper.toDto(user, findUserRole(user));
    }

    public UserProfileDto updateProfile(UUID userId, String fullName, String phone) {
        User user = findExistingUser(userId);

        if (!userPolicy.canUpdateProfile(user)) {
            throw new ApiException(ErrorCode.INVALID_REQUEST);
        }

        User updatedUser = new User(
                user.id(),
                user.roleId(),
                user.email(),
                phone,
                fullName,
                user.avatarUrl(),
                user.status(),
                user.emailVerifiedAt(),
                user.phoneVerifiedAt(),
                user.createdAt(),
                Instant.now()
        );

        User savedUser = userRepository.save(updatedUser);
        return userProfileMapper.toDto(savedUser, findUserRole(savedUser));
    }

    private User findExistingUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private Role findUserRole(User user) {
        return roleRepository.findById(user.roleId())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
