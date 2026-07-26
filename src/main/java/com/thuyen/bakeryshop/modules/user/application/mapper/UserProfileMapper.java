package com.thuyen.bakeryshop.modules.user.application.mapper;

import com.thuyen.bakeryshop.modules.user.application.dto.UserProfileDto;
import com.thuyen.bakeryshop.modules.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileDto toDto(User user) {
        return new UserProfileDto(
                user.id(),
                user.roleId(),
                user.fullName(),
                user.email(),
                user.phone(),
                user.avatarUrl(),
                user.status()
        );
    }
}
