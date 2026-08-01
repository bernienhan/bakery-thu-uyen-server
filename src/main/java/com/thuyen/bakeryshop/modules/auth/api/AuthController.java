package com.thuyen.bakeryshop.modules.auth.api;

import com.thuyen.bakeryshop.common.constant.ApiV1Paths;
import com.thuyen.bakeryshop.common.response.ApiResponse;
import com.thuyen.bakeryshop.common.response.SuccessCode;
import com.thuyen.bakeryshop.common.security.CurrentUserContext;
import com.thuyen.bakeryshop.common.security.SecurityConstants;
import com.thuyen.bakeryshop.modules.auth.api.mapper.AuthApiMapper;
import com.thuyen.bakeryshop.modules.auth.api.request.LoginRequest;
import com.thuyen.bakeryshop.modules.auth.api.request.RegisterRequest;
import com.thuyen.bakeryshop.modules.auth.api.request.UpdateMeRequest;
import com.thuyen.bakeryshop.modules.auth.api.response.LoginResponse;
import com.thuyen.bakeryshop.modules.auth.application.AuthService;
import com.thuyen.bakeryshop.modules.auth.application.dto.LoginResult;
import com.thuyen.bakeryshop.modules.user.api.mapper.UserResponseMapper;
import com.thuyen.bakeryshop.modules.user.api.response.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.time.Instant;

@RestController
@Tag(name = "Auth")
@RequestMapping(ApiV1Paths.Auth.BASE)
public class AuthController {
    private final AuthService authService;
    private final AuthApiMapper authApiMapper;
    private final UserResponseMapper userResponseMapper;

    public AuthController(
            AuthService authService,
            AuthApiMapper authApiMapper,
            UserResponseMapper userResponseMapper
    ){
        this.authService = authService;
        this.authApiMapper = authApiMapper;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping(ApiV1Paths.Auth.REGISTER)
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(authApiMapper.toDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(SuccessCode.REGISTER_SUCCESS, null));
    }

    @PostMapping(ApiV1Paths.Auth.LOGIN)
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        LoginResult result = authService.login(
                authApiMapper.toDto(request),
                httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent")
        );
        LoginResponse response = authApiMapper.toResponse(result);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, sessionCookie(result).toString())
                .body(ApiResponse.ok(SuccessCode.LOGIN_SUCCESS, response));
    }

    @GetMapping(ApiV1Paths.Auth.ME)
    public ApiResponse<UserResponse> me() {
        return ApiResponse.ok(
                SuccessCode.USER_PROFILE_FETCHED,
                userResponseMapper.toResponse(authService.me(CurrentUserContext.require().userId()))
        );
    }

    @PutMapping(ApiV1Paths.Auth.ME)
    public ApiResponse<UserResponse> updateMe(
            @RequestBody UpdateMeRequest request
    ) {
        return ApiResponse.ok(
                SuccessCode.USER_PROFILE_UPDATED,
                userResponseMapper.toResponse(authService.updateMe(
                        CurrentUserContext.require().userId(),
                        authApiMapper.toDto(request)
                ))
        );
    }

    private ResponseCookie sessionCookie(LoginResult result) {
        return ResponseCookie.from(SecurityConstants.SESSION_COOKIE_NAME, result.sessionId().toString())
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.between(Instant.now(), result.expiresAt()))
                .build();
    }
}
