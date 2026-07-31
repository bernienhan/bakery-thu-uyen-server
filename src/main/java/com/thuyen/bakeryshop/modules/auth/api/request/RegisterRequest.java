package com.thuyen.bakeryshop.modules.auth.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
        @Email
        @NotBlank
        String email,
        String phone,
        @NotBlank
        String fullName,
        @NotBlank
        @Size(min = 8)
        String password
){}
