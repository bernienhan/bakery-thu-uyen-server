package com.thuyen.bakeryshop.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_ERROR("Something went wrong"),
    RESOURCE_NOT_FOUND("Resource not found"),
    INVALID_REQUEST("Invalid request");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}
