package com.thuyen.bakeryshop.common.exception;

public enum ErrorCode {
    INTERNAL_ERROR("Something went wrong"),
    RESOURCE_NOT_FOUND("Resource not found"),
    INVALID_REQUEST("Invalid request");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
