package com.thuyen.bakeryshop.modules.user.domain;

public class UserDomainService {

    public boolean canLogin(User user) {
        return user != null;
    }
}
