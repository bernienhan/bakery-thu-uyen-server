package com.thuyen.bakeryshop.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_ERROR("Something went wrong"),
    RESOURCE_NOT_FOUND("Resource not found"),
    INVALID_REQUEST("Invalid request"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    DEFAULT_ROLE_NOT_FOUND("Default role not found");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}
