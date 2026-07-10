package com.thuyen.bakeryshop.modules.user.api.mapper;

import com.thuyen.bakeryshop.modules.user.api.response.UserResponse;
import com.thuyen.bakeryshop.modules.user.application.dto.UserProfileDto;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    public UserResponse toResponse(UserProfileDto profile) {
        return new UserResponse(
                profile.id(),
                profile.roleId(),
                profile.fullName(),
                profile.email(),
                profile.phone(),
                profile.avatarUrl(),
                profile.status()
        );
    }
}
