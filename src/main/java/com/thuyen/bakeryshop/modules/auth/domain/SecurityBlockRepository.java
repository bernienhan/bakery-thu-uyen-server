package com.thuyen.bakeryshop.modules.auth.domain;

import com.thuyen.bakeryshop.modules.auth.domain.model.SecurityBlock;

import java.util.Optional;

public interface SecurityBlockRepository {
    Optional<SecurityBlock> findActiveByTarget(String targetType, String targetValue);

    SecurityBlock save(SecurityBlock securityBlock);
}
