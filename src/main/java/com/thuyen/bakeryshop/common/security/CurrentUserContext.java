package com.thuyen.bakeryshop.common.security;

import com.thuyen.bakeryshop.common.exception.ApiException;
import com.thuyen.bakeryshop.common.exception.ErrorCode;

import java.util.Optional;

public final class CurrentUserContext {
    private static final ThreadLocal<CurrentUser> CURRENT_USER = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    public static void set(CurrentUser currentUser) {
        CURRENT_USER.set(currentUser);
    }

    public static Optional<CurrentUser> get() {
        return Optional.ofNullable(CURRENT_USER.get());
    }

    public static CurrentUser require() {
        return get().orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED));
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}
