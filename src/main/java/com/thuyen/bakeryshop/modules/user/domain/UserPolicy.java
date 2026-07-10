package com.thuyen.bakeryshop.modules.user.domain;

import org.springframework.stereotype.Component;

@Component
public class UserPolicy {

    public boolean canLogin(User user) {
        return user.status() == UserStatus.ACTIVE;
    }

    public boolean canUpdateProfile(User user) {
        return user.status() == UserStatus.ACTIVE;
    }

    public boolean isDeleted(User user) {
        return user.status() == UserStatus.DELETED;
    }
}
