package com.thuyen.bakeryshop.modules.user.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import com.thuyen.bakeryshop.modules.user.infrastructure.persistence.entity.RoleEntity;
import com.thuyen.bakeryshop.modules.user.infrastructure.persistence.entity.UserEntity;
import com.thuyen.bakeryshop.modules.user.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import com.thuyen.bakeryshop.modules.user.domain.User;
import com.thuyen.bakeryshop.modules.user.domain.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryImpl(
            UserJpaRepository userJpaRepository,
            RoleJpaRepository roleJpaRepository,
            UserMapper userMapper
    ) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userJpaRepository.findByPhone(phone).map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        RoleEntity role = roleJpaRepository.getReferenceById(user.roleId());
        UserEntity entity = userMapper.toEntity(user, role);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }
}
