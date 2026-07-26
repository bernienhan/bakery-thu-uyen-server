package com.thuyen.bakeryshop.modules.user.api;

import com.thuyen.bakeryshop.common.response.ApiResponse;
import com.thuyen.bakeryshop.modules.user.api.mapper.UserResponseMapper;
import com.thuyen.bakeryshop.modules.user.api.request.UpdateProfileRequest;
import com.thuyen.bakeryshop.modules.user.api.response.UserResponse;
import com.thuyen.bakeryshop.modules.user.application.UserService;
import com.thuyen.bakeryshop.modules.user.application.dto.UserProfileDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserResponseMapper userResponseMapper;

    public UserController(UserService userService, UserResponseMapper userResponseMapper) {
        this.userService = userService;
        this.userResponseMapper = userResponseMapper;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getProfile(@PathVariable UUID id) {
        return ApiResponse.ok(userResponseMapper.toResponse(userService.getProfile(id)));
    }

    @PutMapping("/{id}/profile")
    public ApiResponse<UserResponse> updateProfile(
            @PathVariable UUID id,
            @RequestBody UpdateProfileRequest request
    ) {
        UserProfileDto profile = userService.updateProfile(id, request.fullName(), request.phone());
        return ApiResponse.ok(userResponseMapper.toResponse(profile));
    }
}
