package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.auth.domain.enums.AuthSessionStatus;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.AuthSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuthSessionJpaRepository extends JpaRepository<AuthSessionEntity, UUID> {
    List<AuthSessionEntity> findByUserIdAndStatus(UUID userId, AuthSessionStatus status);
}
