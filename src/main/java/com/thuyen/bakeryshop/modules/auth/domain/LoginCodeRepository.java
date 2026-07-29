package com.thuyen.bakeryshop.modules.auth.domain;

import com.thuyen.bakeryshop.modules.auth.domain.model.LoginCode;

import java.util.Optional;

public interface LoginCodeRepository {
    Optional<LoginCode> findByCodeHash(String codeHash);

    LoginCode save(LoginCode loginCode);
}
