package com.thuyen.bakeryshop.modules.user.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import com.thuyen.bakeryshop.modules.user.infrastructure.persistence.mapper.RoleMapper;
import org.springframework.stereotype.Repository;

import com.thuyen.bakeryshop.modules.user.domain.Role;
import com.thuyen.bakeryshop.modules.user.domain.RoleRepository;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleMapper roleMapper;

    public RoleRepositoryImpl(RoleJpaRepository roleJpaRepository, RoleMapper roleMapper) {
        this.roleJpaRepository = roleJpaRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findById(id).map(roleMapper::toDomain);
    }

    @Override
    public Optional<Role> findByCode(String code) {
        return roleJpaRepository.findByCode(code).map(roleMapper::toDomain);
    }
}
