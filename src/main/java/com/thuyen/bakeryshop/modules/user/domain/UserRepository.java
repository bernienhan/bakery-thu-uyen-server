package com.thuyen.bakeryshop.modules.user.domain;

import com.thuyen.bakeryshop.modules.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    User save(User user);
}
