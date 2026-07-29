package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.UserCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCredentialJpaRepository extends JpaRepository<UserCredentialEntity, UUID> {
}
