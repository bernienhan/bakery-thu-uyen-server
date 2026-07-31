package com.thuyen.bakeryshop.modules.auth.api.mapper;

import com.thuyen.bakeryshop.modules.auth.api.request.RegisterRequest;
import com.thuyen.bakeryshop.modules.auth.application.dto.RegisterDto;
import org.springframework.stereotype.Component;

@Component
public class AuthApiMapper {

    public RegisterDto toDto(RegisterRequest request) {
        return new RegisterDto(
                request.email(),
                request.phone(),
                request.fullName(),
                request.password()
        );
    }
}
