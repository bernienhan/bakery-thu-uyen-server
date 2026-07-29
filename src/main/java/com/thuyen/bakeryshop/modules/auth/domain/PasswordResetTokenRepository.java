package com.thuyen.bakeryshop.modules.auth.domain;

import com.thuyen.bakeryshop.modules.auth.domain.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository {
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    PasswordResetToken save(PasswordResetToken token);
}
