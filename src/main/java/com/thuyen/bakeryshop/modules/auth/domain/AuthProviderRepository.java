package com.thuyen.bakeryshop.modules.auth.domain;

import com.thuyen.bakeryshop.modules.auth.domain.model.AuthProvider;

import java.util.Optional;
import java.util.UUID;

public interface AuthProviderRepository {
    Optional<AuthProvider> findByProviderCodeAndProviderUserId(String providerCode, String providerUserId);

    Optional<AuthProvider> findByUserIdAndProviderCode(UUID userId, String providerCode);

    AuthProvider save(AuthProvider authProvider);
}
