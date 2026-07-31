package com.thuyen.bakeryshop.modules.auth.api.mapper;

import com.thuyen.bakeryshop.modules.auth.api.request.LoginRequest;
import com.thuyen.bakeryshop.modules.auth.api.request.RegisterRequest;
import com.thuyen.bakeryshop.modules.auth.api.response.LoginResponse;
import com.thuyen.bakeryshop.modules.auth.application.dto.LoginDto;
import com.thuyen.bakeryshop.modules.auth.application.dto.LoginResult;
import com.thuyen.bakeryshop.modules.auth.application.dto.RegisterDto;
import com.thuyen.bakeryshop.modules.user.api.mapper.UserResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthApiMapper {
    private final UserResponseMapper userResponseMapper;

    public AuthApiMapper(UserResponseMapper userResponseMapper) {
        this.userResponseMapper = userResponseMapper;
    }

    public RegisterDto toDto(RegisterRequest request) {
        return new RegisterDto(
                request.email(),
                request.phone(),
                request.fullName(),
                request.password()
        );
    }

    public LoginDto toDto(LoginRequest request) {
        return new LoginDto(
                request.identifier(),
                request.password(),
                request.deviceId(),
                request.deviceName()
        );
    }

    public LoginResponse toResponse(LoginResult result) {
        return new LoginResponse(
                userResponseMapper.toResponse(result.user())
        );
    }
}
