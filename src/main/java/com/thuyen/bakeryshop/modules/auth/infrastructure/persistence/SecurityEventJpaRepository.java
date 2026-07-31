package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.SecurityEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecurityEventJpaRepository extends JpaRepository<SecurityEventEntity, UUID> {
}
