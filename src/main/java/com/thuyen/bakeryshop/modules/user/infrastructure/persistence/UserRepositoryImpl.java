package com.thuyen.bakeryshop.modules.user.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.user.domain.User;
import com.thuyen.bakeryshop.modules.user.domain.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return user;
    }
}
