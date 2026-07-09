package com.thuyen.bakeryshop.modules.auth.api.request;

public record RegisterRequest (
        String userName,
        Number phoneNumber,
        String email,
        String passWord
){}