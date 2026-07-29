package com.thuyen.bakeryshop.modules.auth.domain;

import com.thuyen.bakeryshop.modules.auth.domain.model.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository {
    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    EmailVerificationToken save(EmailVerificationToken token);
}
