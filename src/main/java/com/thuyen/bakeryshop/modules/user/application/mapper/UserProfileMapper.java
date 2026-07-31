package com.thuyen.bakeryshop.modules.user.application.mapper;

import com.thuyen.bakeryshop.modules.user.application.dto.UserProfileDto;
import com.thuyen.bakeryshop.modules.user.domain.model.Role;
import com.thuyen.bakeryshop.modules.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileDto toDto(User user, Role role) {
        return new UserProfileDto(
                user.id(),
                role.code(),
                role.name(),
                user.fullName(),
                user.email(),
                user.phone(),
                user.avatarUrl(),
                user.status()
        );
    }
}
