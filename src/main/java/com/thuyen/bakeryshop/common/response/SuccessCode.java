package com.thuyen.bakeryshop.common.response;

import lombok.Getter;

@Getter
public enum SuccessCode {
    OK("COMMON_SUCCESS_001", "OK"),
    CREATED("COMMON_SUCCESS_002", "Created"),
    UPDATED("COMMON_SUCCESS_003", "Updated successfully"),
    DELETED("COMMON_SUCCESS_004", "Deleted successfully"),

    REGISTER_SUCCESS("AUTH_SUCCESS_001", "Register successfully"),
    LOGIN_SUCCESS("AUTH_SUCCESS_002", "Login successfully"),
    LOGOUT_SUCCESS("AUTH_SUCCESS_003", "Logout successfully"),
    LOGOUT_ALL_SUCCESS("AUTH_SUCCESS_004", "Logout all sessions successfully"),
    VERIFICATION_SENT("AUTH_SUCCESS_005", "Verification sent successfully"),
    VERIFICATION_CONFIRMED("AUTH_SUCCESS_006", "Verification confirmed successfully"),
    PASSWORD_RESET_SENT("AUTH_SUCCESS_007", "Password reset sent successfully"),
    PASSWORD_RESET_SUCCESS("AUTH_SUCCESS_008", "Password reset successfully"),
    PASSWORD_CHANGED("AUTH_SUCCESS_009", "Password changed successfully"),
    SESSION_REVOKED("AUTH_SUCCESS_010", "Session revoked successfully"),

    USER_PROFILE_FETCHED("USER_SUCCESS_001", "User profile fetched successfully"),
    USER_PROFILE_UPDATED("USER_SUCCESS_002", "User profile updated successfully"),

    PRODUCT_CREATED("PRODUCT_SUCCESS_001", "Product created successfully"),
    PRODUCT_UPDATED("PRODUCT_SUCCESS_002", "Product updated successfully"),
    PRODUCT_DELETED("PRODUCT_SUCCESS_003", "Product deleted successfully"),
    PRODUCT_FETCHED("PRODUCT_SUCCESS_004", "Product fetched successfully"),
    PRODUCT_LIST_FETCHED("PRODUCT_SUCCESS_005", "Product list fetched successfully");

    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
