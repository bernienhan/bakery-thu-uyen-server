package com.thuyen.bakeryshop.modules.user.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhone(String phone);
}
