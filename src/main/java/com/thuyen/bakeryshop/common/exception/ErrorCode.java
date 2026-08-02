package com.thuyen.bakeryshop.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_ERROR("COMMON_001", "Something went wrong"),
    INVALID_REQUEST("COMMON_002", "Invalid request"),
    RESOURCE_NOT_FOUND("COMMON_003", "Resource not found"),
    VALIDATION_FAILED("COMMON_004", "Validation failed"),
    UNAUTHORIZED("COMMON_005", "Unauthorized"),
    FORBIDDEN("COMMON_006", "Forbidden"),
    TOO_MANY_REQUESTS("COMMON_007", "Too many requests"),

    EMAIL_ALREADY_EXISTS("AUTH_001", "Email already exists"),
    PHONE_ALREADY_EXISTS("AUTH_002", "Phone already exists"),
    DEFAULT_ROLE_NOT_FOUND("AUTH_003", "Default role not found"),
    INVALID_CREDENTIALS("AUTH_004", "Invalid email or password"),
    ACCOUNT_LOCKED("AUTH_005", "Account is locked"),
    EMAIL_NOT_VERIFIED("AUTH_006", "Email is not verified"),
    VERIFICATION_TOKEN_INVALID("AUTH_007", "Verification token is invalid"),
    VERIFICATION_TOKEN_EXPIRED("AUTH_008", "Verification token has expired"),
    PASSWORD_RESET_TOKEN_INVALID("AUTH_009", "Password reset token is invalid"),
    PASSWORD_RESET_TOKEN_EXPIRED("AUTH_010", "Password reset token has expired"),
    REFRESH_TOKEN_INVALID("AUTH_011", "Refresh token is invalid"),
    REFRESH_TOKEN_EXPIRED("AUTH_012", "Refresh token has expired"),
    SESSION_NOT_FOUND("AUTH_013", "Session not found"),
    SESSION_EXPIRED("AUTH_014", "Session has expired"),
    SESSION_REVOKED("AUTH_015", "Session has been revoked"),

    USER_NOT_FOUND("USER_001", "User not found"),
    USER_INACTIVE("USER_002", "User is inactive"),
    USER_BANNED("USER_003", "User is banned"),
    USER_DELETED("USER_004", "User has been deleted"),
    USER_PROFILE_INVALID("USER_005", "User profile is invalid"),

    PRODUCT_NOT_FOUND("PRODUCT_001", "Product not found"),
    PRODUCT_ALREADY_EXISTS("PRODUCT_002", "Product already exists"),
    PRODUCT_INACTIVE("PRODUCT_003", "Product is inactive"),
    PRODUCT_OUT_OF_STOCK("PRODUCT_004", "Product is out of stock"),
    PRODUCT_PRICE_INVALID("PRODUCT_005", "Product price is invalid"),
    PRODUCT_CATEGORY_NOT_FOUND("PRODUCT_006", "Product category not found");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
