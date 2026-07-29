package com.thuyen.bakeryshop.modules.auth.domain;

import com.thuyen.bakeryshop.modules.auth.domain.model.UserCredential;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepository {
    Optional<UserCredential> findByUserId(UUID userId);

    UserCredential save(UserCredential credential);
}
