package com.thuyen.bakeryshop.modules.auth.api;

import com.thuyen.bakeryshop.common.constant.ApiV1Paths;
import com.thuyen.bakeryshop.common.response.ApiResponse;
import com.thuyen.bakeryshop.modules.auth.api.mapper.AuthApiMapper;
import com.thuyen.bakeryshop.modules.auth.api.request.RegisterRequest;
import com.thuyen.bakeryshop.modules.auth.application.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Tag(name = "Auth")
@RequestMapping(ApiV1Paths.Auth.BASE)
public class AuthController {
    private final AuthService authService;
    private final AuthApiMapper authApiMapper;

    public AuthController(
            AuthService authService,
            AuthApiMapper authApiMapper
    ){
        this.authService = authService;
        this.authApiMapper = authApiMapper;
    }

    @PostMapping(ApiV1Paths.Auth.REGISTER)
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(authApiMapper.toDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(null));
    }
}
